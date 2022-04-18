package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@Entity(name = "ChatMessage")
public class ChatMessage {

    @Id
    @Column(name = "chat_message_id")
    private Long id;

    private String chRoomId;
    private String message;
    private String sender;
    private Date chatDate;
}
