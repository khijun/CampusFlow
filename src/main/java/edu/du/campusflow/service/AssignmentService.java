package edu.du.campusflow.service;

import edu.du.campusflow.dto.AssignmentDTO;
import edu.du.campusflow.entity.Assignment;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.repository.AssignmentRepository;
import edu.du.campusflow.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class AssignmentService {

    @Autowired
    AssignmentRepository assignmentRepository;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    LectureRepository lectureRepository;

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
}
