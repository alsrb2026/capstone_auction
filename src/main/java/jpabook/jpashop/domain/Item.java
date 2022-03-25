package jpabook.jpashop.domain;

import javax.persistence.*;
import java.sql.Timestamp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@NoArgsConstructor
@Getter
@Setter
public class Item { //게시글 관련
    @Id
    @GeneratedValue
    @Column(name="auctionPost_id")
    private Long id;
    private Timestamp postTime; // 게시글 올린 시간
    private String category; //게시글 카테고리
    private String area; //올린 지역
    private int view; //조회수
    private int auctionCount; //입찰 수

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "auctionInfo_id")
    private AuctionInfo auctionInfo;



}