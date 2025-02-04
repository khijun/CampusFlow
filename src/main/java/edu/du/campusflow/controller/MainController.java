package edu.du.campusflow.controller;

import edu.du.campusflow.dto.MemberDTO;
import edu.du.campusflow.entity.Notice;
import edu.du.campusflow.entity.Post;
import edu.du.campusflow.service.AuthService;
import edu.du.campusflow.service.NoticeService;
import edu.du.campusflow.service.PostService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@AllArgsConstructor
@Slf4j
public class MainController {

   private final AuthService authService;
   private final NoticeService noticeService;
   private final PostService postService;

   @GetMapping("/")
   public String frontMain(Model model) {
      model.addAttribute("memberDTO", MemberDTO.fromEntity(authService.getCurrentMember()));
      return "view/main_view/front_main"; // 메인 페이지
   }

   @GetMapping("/iframe/main/main-content")
   public String mainContent(Model model) {
      // 현재 사용자 정보
      model.addAttribute("memberDTO", MemberDTO.fromEntity(authService.getCurrentMember()));

      // 최신 공지사항 5개 가져오기
      Page<Notice> latestNotices = noticeService.getAllNotices(PageRequest.of(0, 5));
      model.addAttribute("noticePage", latestNotices);

      // 로그인중인 사용자의 학과 게시글만 5개 가져오기
      Page<Post> posts = postService.getPostsByDepartment(0, 5);
      model.addAttribute("postPage", posts);

      return "view/iframe/main/main_content";
   }

   @GetMapping("/access-denied")
   public String accessDenied(Model model) {
      return "view/exception/access-denied";
   }


}
