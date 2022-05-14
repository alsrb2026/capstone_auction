package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="post_id")
    private Long id;
    private Long postUserId; // 상품 등록한 사용자 id
    private String postUserName;
    private String title;
    private String contents;
    private String productName;
    private String category;
    private int view;
    private int startBid; // 시작가
    private int winningBid; // 낙찰가 -> 1000, 5000, 10000, 15000, 20000, 50000
    private int unitBid; // 입찰 단위
    private int nextBid; // 현재 입찰가
    private Timestamp regisTime; // 경매 올린 시간 -> yyyy-mm-dd hh:mm:ss
    private int auctionPeriod; // 경매 기간 -> 12시간, 24시간, 36시간, 48시간
    private String status; // 상품 상태 -> 낙찰 중, 낙찰 완료
    private Long currentBidId; // 현재 입찰한 사용자 id
    private String fname;
    // private String time; //작성 시간 -> 물품 등록 시간이랑 겹쳐서 굳이 안써도 될 거 같음

}
