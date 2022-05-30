package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.EntityRepository;
import jpabook.jpashop.service.CertifiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final CertifiService certifiService;
    private final EntityRepository entityRepository;

    @GetMapping("/")
    public String getHome(HttpServletRequest request, Model model) {
        List<Post> posts = entityRepository.findTop5ByOrderByViewDesc();

        model.addAttribute("boardList", posts);

        return "home/home";
    }

    @GetMapping("/login")
    public String getLoginForm(HttpServletRequest request) {
        return "home/loginPage";
    }

    @GetMapping("/user/logout/result")
    public String dispLogout() {
        return "home/home";
    }

    @GetMapping("/adminpage")
    public String getAdminPage() {
        return "home/adminpage";
    }

    @GetMapping("mypage")
    public String getMyPage(Model model) {
        return "home/mypage";
    }
}

