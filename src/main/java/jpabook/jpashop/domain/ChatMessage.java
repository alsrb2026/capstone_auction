package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity(name = "ChatMessage")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String chRoomId;
    private String senderName;
    private String receiverName;
    private Date sendTime;
    private Date recvTime;
    private String message;
    private int checkRead; // checkRead = 0 : 메세지를 읽지 않았다는 의미, 1이면 읽었다는 의미.
}
