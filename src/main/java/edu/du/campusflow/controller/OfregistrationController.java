package edu.du.campusflow.controller;

import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.entity.CommonCode;
import edu.du.campusflow.entity.Lecture;
import edu.du.campusflow.entity.Member;
import edu.du.campusflow.entity.Ofregistration;
import edu.du.campusflow.repository.CommonCodeRepository;
import edu.du.campusflow.repository.LectureRepository;
import edu.du.campusflow.repository.MemberRepository;
import edu.du.campusflow.repository.OfregistrationRepository;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.MemberService;
import edu.du.campusflow.service.OfregistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/iframe/ofregistration")
@RequiredArgsConstructor
public class OfregistrationController {

    private final OfregistrationService ofregistrationService;
    private final AuthService authService;
    private final MemberRepository memberRepository;



    /**
     * 수강신청 가능한 강의 목록 페이지 (페이지만 로드)
     */
    @GetMapping("/student/studentOfre")
    public String showAvailableLectures() {
        return "view/iframe/ofregistration/student/studentOfre";
    }

    /**
     * 수강신청 가능한 강의 목록 데이터 조회 (API)
     */
    @GetMapping("/student/available-lectures")
    public ResponseEntity<List<OfregistrationDTO>> getAvailableLectures() {
        try {
            Member currentStudent = memberRepository.findById(authService.getCurrentMemberId())
                .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));
            
            List<OfregistrationDTO> lectures = ofregistrationService.getAllAvailableLectures(
                currentStudent.getDept().getDeptName()
            );
            return ResponseEntity.ok(lectures);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 수강신청 내역 조회 페이지 (페이지만 로드)
     */
    @GetMapping("/student/stuViewOfre")
    public String viewStudentRegistrations() {
        return "view/iframe/ofregistration/student/stuViewOfre";
    }

    /**
     * 수강신청 내역 데이터 조회 (API)
     */
    @GetMapping("/student/registrations")
    public ResponseEntity<List<OfregistrationDTO>> getStudentRegistrations() {
        try {
            Member currentStudent = memberRepository.findById(authService.getCurrentMemberId())
                    .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));
            
            List<OfregistrationDTO> registrations = ofregistrationService.getStudentRegistrations(
                    currentStudent.getMemberId()
            );
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 내 수강신청 목록 페이지
     */
    @GetMapping("/my-lectures")
    public String showMyLectures(Model model) {
        return "view/iframe/ofregistration/my_lectures";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerLecture(@RequestBody OfregistrationDTO ofregistrationDTO) {
        try {
            // 현재 로그인한 사용자의 ID 가져오기
            Long memberId = authService.getCurrentMemberId();
            if (memberId == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("로그인이 필요합니다.");
            }

            // DTO에 회원 ID 설정
            ofregistrationDTO.setMemberId(memberId);

            // 수강신청 상태를 REQUESTED로 설정
            ofregistrationDTO.setRegStatus("REQUESTED");

            // 수강신청 처리
            ofregistrationService.registerLecture(ofregistrationDTO);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * 관리자용 수강신청 관리 페이지 (페이지만 로드)
     */
    @GetMapping("/admin/adminOfre")
    @PreAuthorize("hasAnyRole('STAFF')")
    public String showAdminOfregistrations() {
        return "view/iframe/ofregistration/admin/adminOfre";
    }

    /**
     * 관리자용 수강신청 관리 데이터 조회 (API)
     */
    @GetMapping("/admin/registrations")
    public ResponseEntity<List<OfregistrationDTO>> getAdminRegistrations() {
        try {
            List<OfregistrationDTO> registrations = ofregistrationService.getPendingRegistrations();
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 수강신청 상태 업데이트 처리
     */
    @PostMapping("/admin/update-status")
    @PreAuthorize("hasAnyRole('STAFF')")
    public ResponseEntity<?> updateRegistrationStatus(@RequestBody OfregistrationDTO ofregistrationDTO) {
        try {
            ofregistrationService.updateRegistrationStatus(
                ofregistrationDTO.getLectureId(),
                ofregistrationDTO.getMemberId(),
                ofregistrationDTO.getRegStatus()
            );
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel/{lectureId}")
    public ResponseEntity<String> cancelRegistration(@PathVariable Long lectureId) {
        try {
            ofregistrationService.cancelRegistration(lectureId);
            return ResponseEntity.ok("수강신청이 취소되었습니다.");
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}