package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class PostUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="postuser_id")
    private Long id;
    private Long postId; // 상품 등록한 사용자 id
    private String bidUserAccountId;
    private String bidUserName; //입찰한 사용자의 닉네임
    private String postUserAccountId;
    private String postUserName; //경매글 등록자 닉네임
    private int bid;
    private Date bidDate;
    private String type; // 입찰, 낙찰, 즉시 구매 인지.

}
