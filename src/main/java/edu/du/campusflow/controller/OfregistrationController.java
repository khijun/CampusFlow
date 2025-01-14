package edu.du.campusflow.controller;

import edu.du.campusflow.dto.OfregistrationDTO;
import edu.du.campusflow.service.OfregistrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/iframe/ofregistration")
@RequiredArgsConstructor
public class OfregistrationController {

    private final OfregistrationService ofregistrationService;

    /**
     * 수강신청 가능한 강의 목록 페이지
     */
    @GetMapping("/student/studentOfre")
    public String showAvailableLectures(Model model) {
        log.info("수강 가능한 강의 목록 조회");
        try {
            List<OfregistrationDTO> lectures = ofregistrationService.getAllAvailableLectures();
            model.addAttribute("lectures", lectures);
            return "view/iframe/ofregistration/student/studentOfre";
        } catch (Exception e) {
            log.error("강의 목록 조회 중 오류 발생", e);
            throw e;
        }
    }

    /**
     * 내 수강신청 목록 페이지
     */
    @GetMapping("/my-lectures")
    public String showMyLectures(Model model) {
        // TODO: 현재 로그인한 사용자의 수강신청 목록 조회 로직 구현
        return "view/iframe/ofregistration/my_lectures";
    }
}