package jpabook.jpashop.controller;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final PostService postService;

    @GetMapping("/")
    public String getHome(HttpServletRequest request) {

        return "home/home";
    }

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest request) {
        return "home/loginPage";
    }

    // 로그인 성공하면 아래 작업하고 home으로 이동
    /*
    @PostMapping("/doLogin")
    public String dispLoginResult(HttpServletRequest request) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        UserEntity user = userRepository.findByName(username).get();

        System.out.println("하핳" + user.getUserId());

        request.setAttribute("user",user);

        return "home/home";
    }*/

    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "home/home";
    }

//    // 내 정보 페이지
//    @GetMapping("/user/info")
//    public String dispMyInfo() {
//        return "/myinfo";
//    }

    @GetMapping("/adminpage")
    public String getAdminPage() {
        return "home/adminpage";
    }

    @GetMapping("mypage")
    public String getMyPage(Model model) {

        return "home/mypage";
    }

}

