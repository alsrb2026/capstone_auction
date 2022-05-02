package jpabook.jpashop.controller;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.PostRepository;
import jpabook.jpashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest request) {
        return "loginPage";
    }

    // 로그인 결과 페이지
    @GetMapping("/user/login/result")
    public String dispLoginResult(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        UserEntity user = userRepository.findByName(username).get();
        request.setAttribute("user",user);
        return "home";
    }

    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "home";
    }

    // 내 정보 페이지
    @GetMapping("/user/info")
    public String dispMyInfo() {
        return "/myinfo";
    }

    //어드민 페이지
    @GetMapping("/admin")
    public String dispAdmin() {
        return "/admin";
    }


    @GetMapping("/")
    public String getHome(HttpServletRequest request) {
        return "home";
    }

    @GetMapping("/adminpage")
    public String getAdminPage() {
        return "adminpage";
    }

    @GetMapping("/mypage")
    public String getMyPage(Model model) {

        return "mypage";
    }

}

