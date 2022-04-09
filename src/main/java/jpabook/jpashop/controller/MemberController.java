package jpabook.jpashop.controller;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.repository.UserRepositoryR;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;



@Controller
@RequiredArgsConstructor
public class MemberController {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRepositoryR userRepositoryR;


    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new UserForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid UserForm form, BindingResult result, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        //Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        String name = form.getId();
        String nickname = form.getNickname();
        String passwd = form.getPasswd();

        UserEntity user = UserEntity.builder()
                .name(name)
                .nickname(nickname)
                .password(passwordEncoder.encode(passwd))
                .role("user")
                .build();

        System.out.println(name+passwd);
        userRepositoryR.save(user);
        return "redirect:/";
    }
    /*public String create(@Valid MemberForm form, BindingResult result){

        if(result.hasErrors()){
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/";
    }*/

//    @GetMapping("/members")
//    public String List(Model model) {
//        List<Member> members = memberService.findMembers();
//        model.addAttribute("members",members);
//        return "members/memberList";
//    }
}
