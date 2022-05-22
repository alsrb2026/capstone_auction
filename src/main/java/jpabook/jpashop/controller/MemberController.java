package jpabook.jpashop.controller;

import jpabook.jpashop.Form.UserForm;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;


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
    public String create(@Valid UserForm form, Model model) {


        String name = form.getId();
        String nickname = form.getNickname();
        String phoneNumber = form.getPhoneNumber();
        String passwd = form.getPasswd();


        //db에 유저가 이미 존재하면 정보 가져오기
        Optional<UserEntity> dbUser = userRepositoryR.findByNickname(nickname);

        if (dbUser.isPresent()) { //닉네임 중복이면
            String dbNickname = dbUser.get().getNickname();
            System.out.println("dbNIckname = " + dbNickname);
            model.addAttribute("memberForm", new UserForm());
            return "alert";
            //return "members/createMemberForm";
        } else { //중복 없으면 회원정보 저장
            UserEntity user = UserEntity.builder()
                    .name(name)
                    .nickname(nickname)
                    .phoneNumber(phoneNumber)
                    .password(passwordEncoder.encode(passwd))
                    .role("user")
                    .build();

            userRepositoryR.save(user);
            return "home/loginPage";
        }
    }


}
