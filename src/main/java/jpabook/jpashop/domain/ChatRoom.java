package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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

    public static ChatRoom create(String name) {
        ChatRoom room = new ChatRoom();

        room.roomId = UUID.randomUUID().toString();
        room.name = name;
        return room;
    }
}

