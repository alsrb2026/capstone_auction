package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.awt.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@Getter
@Setter
public class Product { //상품관련
    @Id
    @GeneratedValue
    @Column(name="product_id")
    private Long id;
    private Long itemUserId; // 상품 올린 사용자 id
    private String name; // 상품 이름
    private String title; // 상품 제목
    private String description; // 상품 설명
    private Image[] images; // 상품 이미지
}