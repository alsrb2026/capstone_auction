package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "ChatRoom")
public class ChatRoom {

    @Id
    @GeneratedValue(generator = "room-uuid")
    @GenericGenerator(name = "room-uuid", strategy = "uuid")
    @Column(name = "room_id")
    private String roomId;
    private String name;
    private Long regisUserId;
    private Long auctionUserId;
    private String regisName;
    private String buyerName;
    public static ChatRoom create(String name, Long id1, Long id2, String regisName, String buyerName) {
        // id1 : 판매자, id2 : 구매자
        ChatRoom room = new ChatRoom();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        room.regisUserId = id1;
        room.auctionUserId = id2;
        room.regisName = regisName;
        room.buyerName = buyerName;

        return room;
    }
}

