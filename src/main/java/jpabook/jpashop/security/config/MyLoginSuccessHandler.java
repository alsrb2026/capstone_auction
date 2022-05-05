package jpabook.jpashop.security.config;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@RequiredArgsConstructor
public class MyLoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        HttpSession session = request.getSession();

        UserEntity user = userRepository.findByName(authentication.getName()).get();

        Long id = user.getUserId();
        String accountId = user.getName();
        String nickname = user.getNickname();

        session.setAttribute("id", id);
        session.setAttribute("accountId", accountId);
        session.setAttribute("nickname", nickname);

        response.sendRedirect("/");
    }
}