package jpabook.jpashop.domain;//package jpabook.jpashop.domain;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//import javax.persistence.*;
//import java.awt.*;
//import java.sql.Timestamp;
//
//@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@NoArgsConstructor
//@Getter
//@Setter
//public class Item {
//
//    @Id @GeneratedValue
//    @Column(name="item_id")
//    private Long id;
//    private Long itemUserId; // 상품 올린 사용자 id
//    @Column(name="item_name")
//    private String name; // 상품 이름
//    private String title; // 상품 제목
//    private int startBid; // 시작가
//    private int winningBid; // 낙찰가 -> 1000, 5000, 10000, 15000, 20000, 50000
//    private int unitBid; // 입찰 단위
//    private String description; // 상품 설명
//    private String status; // 상품 상태 -> 낙찰 중, 낙찰 완료
//    private Image[] images; // 상품 이미지
//    private Long currentBidId; // 현재 입찰한 사용자 id
//    private int currentBid; // 현재 입찰가
//    private Timestamp startAuctionTime; // 경매 올린 시간 -> yyyy-mm-dd hh:mm:ss
//    private int auctionPeriod; // 경매 기간 -> 12시간, 24시간, 36시간, 48시간
//
//    public class Product { //상품관련
//        @Id @GeneratedValue
//        @Column(name="product_id")
//        private Long id;
//        private Long itemUserId; // 상품 올린 사용자 id
//        private String name; // 상품 이름
//        private String title; // 상품 제목
//        private String description; // 상품 설명
//        private Image[] images; // 상품 이미지
//    }
//
//    public class AuctionInfo { //경매관련
//        @Id @GeneratedValue
//        @Column(name="auctionInfo_id")
//        private Long id;
//        private int startBid; // 시작가
//        private int winningBid; // 낙찰가 -> 1000, 5000, 10000, 15000, 20000, 50000
//        private int unitBid; // 입찰 단위
//        private int currentBid; // 현재 입찰가
//        private Timestamp startAuctionTime; // 경매 올린 시간 -> yyyy-mm-dd hh:mm:ss
//        private int auctionPeriod; // 경매 기간 -> 12시간, 24시간, 36시간, 48시간
//        private String status; // 상품 상태 -> 낙찰 중, 낙찰 완료
//        private Long currentBidId; // 현재 입찰한 사용자 id
//    }
//
//    public class ActionPost { //게시글 관련
//        private Timestamp postTime; // 게시글 올린 시간
//        private String category; //게시글 카테고리
//        private String area; //올린 지역
//        private int view; //조회수
//        private int auctionCount; //입찰 수
//
//        @OneToOne(cascade = CascadeType.PERSIST)
//        @JoinColumn(name = "product_id")
//        private Product product;
//
//        @OneToOne(cascade = CascadeType.PERSIST)
//        @JoinColumn(name = "auctionInfo_id")
//        private AuctionInfo auctionInfo;
//    }
//
//   @Builder
//    public Item(Long id, Long itemUserId, String name, String title, int startBid, int winningBid, int unitBid, String description, String status, Image[] images,
//                Long currentBidId, Timestamp startAuctionTime, int auctionPeriod) {
//        this.id = id;
//        this.itemUserId = itemUserId;
//        this.name = name;
//        this.title = title;
//        this.startBid = startBid;
//        this.winningBid = winningBid;
//        this.unitBid = unitBid;
//        this.description = description;
//        this.status = status;
//        this.images = images;
//        this.currentBidId = currentBidId;
//        this.currentBid = startBid;
//        this.startAuctionTime = startAuctionTime;
//        this.auctionPeriod = auctionPeriod;
//    }
//}
