package edu.du.campusflow.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        // 포트폴리오 로그인 정보 자동 입력용 코드
                        @RequestParam(value = "id", required = false) String id,
                        @RequestParam(value = "pw", required = false) String pw,
                        // ###
                        Model model) {

        if (error != null) {
            model.addAttribute("errorMessage", "아이디 또는 비밀번호가 올바르지 않습니다.");
        }

        if (logout != null) {
            model.addAttribute("logoutMessage", "로그아웃 되었습니다.");
        }

        // 포트폴리오 로그인 정보 자동 입력용 코드
        if(!(id==null||id.isEmpty())){
            model.addAttribute("id", id);
        }
        if(!(pw==null||pw.isEmpty())){
            model.addAttribute("pw", pw);
        }

        return "view/main_view/login";
    }
}
