package edu.du.campusflow.controller;

import edu.du.campusflow.dto.LectureDTO;
import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.entity.FileInfo;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.LectureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Controller
public class LectureController {

    @Autowired
    LectureService lectureService;

    @Autowired
    AuthService authService;

    @GetMapping("/iframe/lecture/create")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String lectureCreate(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/lecture/professor/lectureCreate";
    }

    //강의 개설에 사용
    @PostMapping("/api/lecture/create")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public ResponseEntity<String> createLecture(@RequestBody LectureDTO lectureDTO) {
        try {
            lectureService.createLecture(lectureDTO);
            return ResponseEntity.ok("강의 개설 신청이 성공적으로 완료되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/iframe/lecture/lectureApproval")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String lectureApproval(Model model) {
        return "view/iframe/lecture/admin/lectureApproval";
    }

    @GetMapping("/api/lecture/search")  // lectures/search -> lecture/search
    @ResponseBody
    public List<LectureDTO> searchLectures(
            @RequestParam(required = false) String semesterCode,
            @RequestParam(required = false) String deptName,
            @RequestParam(required = false) String professorName) {
        return lectureService.searchLectures(semesterCode, deptName, professorName);
    }

    @PostMapping("/api/lecture/approve")
    @PreAuthorize("hasAnyRole('STAFF')")
    @ResponseBody
    public ResponseEntity<String> approveLecture(@RequestBody LectureDTO lectureDTO) {
        try {
            lectureService.approveLecture(lectureDTO);
            return ResponseEntity.ok("강의가 승인되었습니다.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/iframe/lecture/createList")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String lectureCreateList(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/lecture/professor/lectureCreate_List";
    }

    @GetMapping("/api/lecture/approvedList")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public List<LectureDTO> getApprovedLectures(
            @RequestParam(required = false) String semesterCode,
            @RequestParam(required = false) String professorId) {
        return lectureService.getApprovedLectures(semesterCode, professorId);
    }

    @GetMapping("/api/lecture/pendingList")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public List<LectureDTO> getPendingLectures(
            @RequestParam(required = false) String semesterCode,
            @RequestParam(required = false) String professorId) {
        return lectureService.getPendingLectures(semesterCode, professorId);
    }

    @GetMapping("/iframe/lecture/lectureUploadFile")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String lectureFile(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/lecture/professor/lecture_File";
    }

    @GetMapping("/api/lecture/lectureSearch")
    @ResponseBody
    public List<LectureDTO> searchLecturesForFile(
            @RequestParam String semesterCode,
            @RequestParam String professorId) {
        return lectureService.getApprovedLectures(semesterCode, professorId);
    }

    @PostMapping("/api/lecture/upload-file")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    @ResponseBody
    public ResponseEntity<String> uploadLectureFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("lectureId") Long lectureId) {
        try {
            // LectureService의 uploadLectureFile 메서드 호출
            lectureService.uploadLectureFile(file, lectureId);
            return ResponseEntity.ok("강의계획서가 성공적으로 업로드되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/iframe/lecture/lectureFile_List")
    @PreAuthorize("hasAnyRole('STAFF', 'PROFESSOR')")
    public String lectureFileList(Model model) {
        model.addAttribute("member", authService.getCurrentMember());
        return "view/iframe/lecture/professor/lectureFile_List";
    }

    //강의 계획서 조회 페이지 에서 사용할 검색
    @GetMapping("/api/lecture/file-search")
    @ResponseBody
    public List<LectureDTO> searchLecturesForUpLoadFile(
            @RequestParam String semesterCode,
            @RequestParam String professorId) {
        return lectureService.getApprovedLectures(semesterCode, professorId);
    }

    //강의계획서 조회
    @GetMapping("/api/file/view/{fileId}")
    public ResponseEntity<Resource> viewFile(@PathVariable Long fileId) {
        try {
            FileInfo fileInfo = lectureService.getFileInfo(fileId);
            Resource resource = lectureService.getFileResource(fileId);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + URLEncoder.encode(fileInfo.getFileName(), "UTF-8") + ".pdf\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("파일을 불러오는데 실패했습니다.");
        }
    }

    //강의 계획서 조회 페이지 에서 사용할 검색
    @GetMapping("/api/lecture/professorId")
    @ResponseBody
    public List<LectureDTO> searchLectureAssignment(
            @RequestParam String semesterCode,
            @RequestParam String professorId) {
        return lectureService.getApprovedLectures(semesterCode, professorId);
    }

    //과제 제출페이지 학생이 수강중인 강의 검색
    @GetMapping("/api/lecture/student")
    @ResponseBody
    public ResponseEntity<List<OfregistrationDTO>> getStudentLectures(
            @RequestParam String semesterCode,
            @RequestParam String studentId) {
        try {
            List<OfregistrationDTO> lectures = lectureService.getStudentLectures(semesterCode, studentId);
            return ResponseEntity.ok(lectures);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.emptyList());
        }
    }
}
