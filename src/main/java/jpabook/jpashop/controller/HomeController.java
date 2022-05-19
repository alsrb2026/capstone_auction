package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.EntityRepository;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;
    private final PostService postService;
    private final EntityRepository entityRepository;

    @GetMapping("/")
    public String getHome(HttpServletRequest request, Model model) {
        List<Post> posts = entityRepository.findTop5ByOrderByViewDesc();
        for(Post post : posts){
            System.out.println(post.getTitle());
            System.out.println(post.getView());
        }
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

