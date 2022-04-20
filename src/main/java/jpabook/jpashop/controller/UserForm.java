package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
public class UserForm {//test3
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String id;
    private String nickname;
    private List<String> likeList;
    private String passwd;
}