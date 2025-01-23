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
     * 수강신청 가능한 강의 목록 페이지
     */
    @GetMapping("/student/studentOfre")
    public String showAvailableLectures(Model model) {
        // 현재 로그인한 학생의 정보 가져오기
        Member currentStudent = memberRepository.findById(authService.getCurrentMemberId())
            .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));
        
        // 학생의 학과명과 함께 수강 가능한 강의 목록 조회
        List<OfregistrationDTO> lectures = ofregistrationService.getAllAvailableLectures(
            currentStudent.getDept().getDeptName()
        );
        
        model.addAttribute("lectures", lectures);
        return "view/iframe/ofregistration/student/studentOfre";
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
     * 수강신청 내역 조회 페이지
     */
    @GetMapping("/student/stuViewOfre")
    public String viewStudentRegistrations(Model model) {
        // 현재 로그인한 학생의 정보 가져오기
        Member currentStudent = memberRepository.findById(authService.getCurrentMemberId())
                .orElseThrow(() -> new IllegalStateException("로그인 정보를 찾을 수 없습니다."));
        
        // 학생의 수강신청 내역 조회
        List<OfregistrationDTO> registrations = ofregistrationService.getStudentRegistrations(
                currentStudent.getMemberId()
        );
        
        model.addAttribute("registrations", registrations);
        return "view/iframe/ofregistration/student/stuViewOfre";
    }

    /**
     * 관리자용 수강신청 관리 페이지
     */
    @GetMapping("/admin/adminOfre")
    public String showAdminOfregistrations(Model model) {
        // 수강신청 대기 상태인 강의 목록 조회
        List<OfregistrationDTO> registrations = ofregistrationService.getPendingRegistrations();
        model.addAttribute("registrations", registrations);
        return "view/iframe/ofregistration/admin/adminOfre";
    }

    /**
     * 수강신청 상태 업데이트 처리
     */
    @PostMapping("/admin/update-status")
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