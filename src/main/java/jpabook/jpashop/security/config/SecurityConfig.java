package jpabook.jpashop.security.config;

import jpabook.jpashop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserRepository userRepository;

    @Override
    public void configure(WebSecurity webSecurity) throws Exception {
        webSecurity.ignoring().antMatchers(
                "../../../photo/**","../../../postimages/**","/postimages/**","/photo/**","/resources/static/css/**", "/css/**", "/fonts/**", "/js/**", "/less/**", "/scss/**", "/images/**", "/resources/static/images/**", "/resources/static/js/**",  "/webjars/**");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .headers(headers -> headers .cacheControl(cache -> cache.disable()))
                .authorizeRequests()
                .antMatchers("/login","/members/new","/","/posts","/post/search",
                        "/post/searchCategory/{keyword}","post/{id}/auction","/sms","/check/sendSMS")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/doLogin")
                .usernameParameter("id")
                .passwordParameter("pw")
                .successHandler(new MyLoginSuccessHandler(userRepository))
                .and()
                .logout()
                .logoutUrl("/doLogout")
                .logoutSuccessUrl("/login");
    }

}
