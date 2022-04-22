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
    private static ArrayList<Long> id = new ArrayList<>(2);

    public static ChatRoom create(String name, Long id1, Long id2) {
        ChatRoom room = new ChatRoom();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        id.add(id1);
        id.add(id2); // 구매자와 물품주인의 id만 넣어준다.

        return room;
    }
}

