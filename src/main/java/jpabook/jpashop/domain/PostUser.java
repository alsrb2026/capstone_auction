package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class PostUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="postuser_id")
    private Long id;
    private Long postId; // 상품 등록한 사용자 id
    private String bidUserName; //입찰한 사용자의 닉네임
    private String PostUserName; //경매글 등록자 닉네임

}
