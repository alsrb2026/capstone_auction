package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Post {
    @Id
    @GeneratedValue
    @Column(name="post_id")
    private Long id;
    private String title;
    private String contents;
    private String product_name;
    private int view;
    private int startBid; // 시작가
    private int winningBid; // 낙찰가 -> 1000, 5000, 10000, 15000, 20000, 50000
    private int unitBid; // 입찰 단위
    private int currentBid; // 현재 입찰가
    //private Timestamp startAuctionTime; // 경매 올린 시간 -> yyyy-mm-dd hh:mm:ss
    private int auctionPeriod; // 경매 기간 -> 12시간, 24시간, 36시간, 48시간
    private String status; // 상품 상태 -> 낙찰 중, 낙찰 완료
    private Long currentBidId; // 현재 입찰한 사용자 id
    private String time; //작성 시간

}
