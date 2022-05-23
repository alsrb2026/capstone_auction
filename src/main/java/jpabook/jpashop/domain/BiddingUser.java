package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class BiddingUser {

    @Id
    @GeneratedValue
    private Long id;
    private String BiddingUsername;

}
