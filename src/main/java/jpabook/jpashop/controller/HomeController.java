package jpabook.jpashop.controller;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UserRepository userRepository;

    /*@GetMapping("/signUp")
    public String signUp() {
        UserEntity user = UserEntity.builder()
                .name("galid")
                .password(passwordEncoder.encode("1234"))
                .role("user")
                .build();

        System.out.println("save?");
        userRepository.save(user);

        return "redirect:/login";
    }*/

    @GetMapping("/login")
    public String getLoginForm() {
        return "loginPage";
    }

    @GetMapping("/")
    public String getHome(HttpServletRequest request) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails)principal).getUsername();
        UserEntity user = userRepository.findByName(username).get();

        request.setAttribute("user",user);
        request.setAttribute("user",user);
        return "home";
    }

    @GetMapping("/adminpage")
    public String getAdminPage() {
        return "adminpage";
    }

//    @GetMapping("/home")
//    public String getMain() {
//        return "home";
//    }
}

