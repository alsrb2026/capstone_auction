package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class UserForm {
    @NotEmpty(message = "회원 이름은 필수 입니다")
    private String name;
    private String passwd;
    private String city;
    private String street;
    private String zipcode;

}