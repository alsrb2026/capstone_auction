package jpabook.jpashop.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


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
    public String getIndex() {
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

