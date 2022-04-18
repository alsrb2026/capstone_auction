package jpabook.jpashop.controller;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate template;

    //Client가 SEND할 수 있는 경로
    //stompConfig에서 설정한 applicationDestinationPrefixes와 @MessageMapping 경로가 병합됨
    //"/pub/chat/enter"
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessage message) throws Exception {
        message.setMessage(message.getSender() + "님이 채팅방에 참여하였습니다.");
        template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);

    }

    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessage message) throws Exception {
        message.setMessage(message.getSender() + "님이 채팅방에서 나갔습니다.");
        template.convertAndSend("/sub/chat/room" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessage message) throws Exception {
        message.setMessage(message.getSender() + ": " + message.getMessage());
        template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    }
}
