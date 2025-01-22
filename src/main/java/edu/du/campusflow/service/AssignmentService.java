package edu.du.campusflow.service;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    LectureRepository lectureRepository;

    @Autowired
    SubmissionRepository submissionRepository;

    @Autowired
    OfregistrationRepository ofregistrationRepository;

    @Autowired
    CommonCodeRepository commonCodeRepository;

    @Autowired
    FileInfoRepository fileInfoRepository;

    @Autowired
    LectureService lectureService;

    @Transactional
    public void createAssignment(AssignmentDTO assignmentDTO, MultipartFile file) {
        // 1. 강의 조회
        Lecture lecture = lectureRepository.findById(assignmentDTO.getLectureId())
                .orElseThrow(() -> new RuntimeException("해당 강의를 찾을 수 없습니다."));

        // 2. 파일 업로드 처리 (파일이 있는 경우)
        FileInfo fileInfo = null;
        if (file != null && !file.isEmpty()) {
            fileInfo = fileUploadService.saveFile(file);
        }

        // 3. Assignment 엔티티 생성
        Assignment assignment = new Assignment();
        assignment.setLecture(lecture);
        assignment.setAssignmentName(assignmentDTO.getAssignmentName());
        assignment.setAssignmentDesc(assignmentDTO.getDescription());
        // 단순히 LocalDate를 LocalDateTime으로 변환
        LocalDateTime dueDate = assignmentDTO.getDueDate().atTime(0, 0);
        assignment.setDueDate(dueDate);
        assignment.setFileInfo(fileInfo);

        // 4. 저장
        assignmentRepository.save(assignment);
    }

    //강의에 있는 과제 검색
    public List<AssignmentDTO> getStudentAssignments(Long lectureId) {
        // 1. 강의 존재 여부 확인
        Lecture lecture = lectureRepository.findById(lectureId)
                .orElseThrow(() -> new RuntimeException("해당 강의를 찾을 수 없습니다."));

        // 2. 해당 강의의 모든 과제 조회
        List<Assignment> assignments = assignmentRepository.findByLecture(lecture);

        // 3. DTO로 변환
        return assignments.stream()
                .map(assignment -> {
                    AssignmentDTO dto = new AssignmentDTO();
                    dto.setAssignmentId(assignment.getAssignmentId());
                    dto.setLectureId(assignment.getLecture().getLectureId());
                    dto.setLectureName(assignment.getLecture().getLectureName());
                    dto.setProfessorName(assignment.getLecture().getMember().getName());
                    dto.setAssignmentName(assignment.getAssignmentName());
                    dto.setDescription(assignment.getAssignmentDesc());
                    dto.setDueDate(assignment.getDueDate().toLocalDate());
                    // 첨부파일이 있는 경우
                    if (assignment.getFileInfo() != null) {
                        dto.setFileInfo(assignment.getFileInfo().getId());
                    }
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void submitAssignment(Long assignmentId, Member student, MultipartFile file) {
        // 1. 과제 존재 여부 확인
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("해당 과제를 찾을 수 없습니다."));

        // 2. 수강신청 정보 조회
        Ofregistration ofregistration = ofregistrationRepository
                .findByLectureIdAndMemberId(assignment.getLecture().getLectureId(), student.getMemberId())
                .orElseThrow(() -> new RuntimeException("수강 중인 강의가 아닙니다."));

        // 3. 중복 제출 확인
        Optional<Submission> existingSubmission = submissionRepository
                .findByAssignmentAndOfregistration(assignment, ofregistration);
        if (existingSubmission.isPresent()) {
            throw new RuntimeException("이미 제출한 과제입니다.");
        }

        // 4. 제출 기한 확인
        if (LocalDateTime.now().isAfter(assignment.getDueDate())) {
            throw new RuntimeException("제출 기한이 지났습니다.");
        }

        // 5. 파일 업로드
        FileInfo fileInfo = fileUploadService.saveFile(file);

        // 6. Submission 생성 및 저장
        Submission submission = new Submission();
        submission.setAssignment(assignment);
        submission.setFileInfo(fileInfo);
        submission.setSubmissionDate(LocalDateTime.now());
        submission.setOfregistration(ofregistration);

        submissionRepository.save(submission);
    }

    // AssignmentService에 추가할 메서드
    public List<AssignmentDTO> getProfessorAssignments(String semesterCode, String professorId, String lectureId) {
        // 1. 학기 코드로 CommonCode 조회
        CommonCode semester = commonCodeRepository.findByCodeValue(semesterCode);
        if (semester == null) {
            throw new RuntimeException("잘못된 학기 코드입니다.");
        }

        // 2. 강의 목록 조회 조건 설정
        List<Lecture> lectures;
        if (lectureId != null && !lectureId.isEmpty()) {
            // 특정 강의만 조회
            lectures = Collections.singletonList(
                    lectureRepository.findById(Long.parseLong(lectureId))
                            .orElseThrow(() -> new RuntimeException("해당 강의를 찾을 수 없습니다."))
            );
        } else {
            // 교수의 해당 학기 전체 강의 조회
            lectures = lectureRepository.findByMember_MemberIdAndSemester(professorId, semester);
        }

        // 3. 각 강의의 과제 목록 조회 및 DTO 변환
        return lectures.stream()
                .flatMap(lecture -> assignmentRepository.findByLecture(lecture).stream())
                .map(assignment -> {
                    AssignmentDTO dto = new AssignmentDTO();
                    dto.setAssignmentId(assignment.getAssignmentId());
                    dto.setLectureName(assignment.getLecture().getLectureName());
                    dto.setAssignmentName(assignment.getAssignmentName());
                    dto.setDueDate(assignment.getDueDate().toLocalDate());

                    // 제출 인원 카운트
                    Integer submissionCount = submissionRepository.countByAssignment(assignment);
                    dto.setSubmissionCount(submissionCount);

                    return dto;
                })
                .collect(Collectors.toList());
    }

    //과제 제출 리스트 조회
    public List<AssignmentDTO> getSubmissionList(Long assignmentId) {
        // 1. 과제 존재 여부 확인
        Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("해당 과제를 찾을 수 없습니다."));

        // 2. 과제에 대한 모든 제출 정보 조회
        List<Submission> submissions = submissionRepository.findByAssignment(assignment);

        // 3. DTO로 변환
        return submissions.stream()
                .map(submission -> {
                    AssignmentDTO dto = new AssignmentDTO();
                    dto.setStudentId(submission.getOfregistration().getMember().getMemberId());
                    dto.setDeptName(submission.getOfregistration().getMember().getDept().getDeptName());
                    dto.setStudentName(submission.getOfregistration().getMember().getName());
                    dto.setSubmissionDate(submission.getSubmissionDate());
                    if (submission.getFileInfo() != null) {
                        dto.setFileInfo(submission.getFileInfo().getId());
                    }
                    dto.setAssignmentScore(submission.getAssignmentScore());
                    dto.setSubmissionId(submission.getSubmissionId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    //제출 파일의 파일 타입에 따른 뷰어
    public String getFileViewerHtml(Long fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));

        String fileName = fileInfo.getFileName();
        String fileType = fileInfo.getFileType().toLowerCase();

        // 디버깅을 위한 로그 출력
        System.out.println("파일명: " + fileName);
        System.out.println("파일 타입: " + fileType);

        if ("pdf".equals(fileType)) {
            return String.format("<iframe src='/api/file/submission/%d' style='width:100%%; height:100%%; border:none;'></iframe>", fileId);
        } else if (Arrays.asList("png", "jpg", "jpeg", "gif").contains(fileType)) {
            return String.format("<img src='/api/file/submission/%d' style='max-width:100%%; max-height:100%%; object-fit:contain;'>", fileId);
        } else {
            return String.format(
                    "<div style='text-align:center; padding:20px;'>" +
                            "<p>이 파일 형식은 미리보기를 지원하지 않습니다.</p>" +
                            "<a href='/api/file/download/%d' class='btn btn-primary'>다운로드</a>" +
                            "</div>", fileId);
        }
    }

    // MIME 타입 변환 메서드
    // 파일 확장자(png, jpg 등)를 HTTP Content-Type(image/png, image/jpeg 등)으로 변환
    private String getMimeType(String fileType) {
        fileType = fileType.toLowerCase();
        switch (fileType) {
            case "png":
                return "image/png";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "gif":
                return "image/gif";
            case "pdf":
                return "application/pdf";
            default:
                return "application/octet-stream";
        }
    }

    // 제출된 파일을 다운로드/뷰어로 보여주기 위한 응답 생성 메서드
    // 파일 정보와 리소스를 가져와서 브라우저에서 볼 수 있는 형태의 HTTP 응답으로 변환
    public ResponseEntity<Resource> getSubmissionFileResponse(Long fileId) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId)
                .orElseThrow(() -> new RuntimeException("파일을 찾을 수 없습니다."));
        Resource resource = lectureService.getFileResource(fileId);
        String contentType = getMimeType(fileInfo.getFileType());
        // 파일명 인코딩 처리
        String encodedFileName;
        try {
            encodedFileName = URLEncoder.encode(fileInfo.getFileName(), "UTF-8")
                    .replaceAll("\\+", "%20");  // 공백 처리
        } catch (UnsupportedEncodingException e) {
            encodedFileName = "download";  // 인코딩 실패시 기본값
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + encodedFileName + "\"; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    // 과제 점수 등록 서비스
    @Transactional
    public void updateSubmissionScore(Long submissionId, Integer score) {
        // 1. 점수 유효성 검사
        if (score < 0 || score > 100) {
            throw new RuntimeException("점수는 0-100 사이여야 합니다.");
        }

        // 2. 제출 정보 조회
        Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("제출 정보를 찾을 수 없습니다."));

        // 3. 점수 업데이트
        submission.setAssignmentScore(score);
        submissionRepository.save(submission);
    }
}
