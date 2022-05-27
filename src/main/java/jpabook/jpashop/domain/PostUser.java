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
    private Long bidUserName; //입찰한 사용자의 userid

}
