package edu.du.campusflow.service;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.entity.*;
import edu.du.campusflow.repository.AssignmentRepository;
import edu.du.campusflow.repository.LectureRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import edu.du.campusflow.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;
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

}
