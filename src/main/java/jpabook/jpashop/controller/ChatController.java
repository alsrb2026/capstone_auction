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

    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessage message) throws Exception {
        template.convertAndSend("/sub/chat/room" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessage message) throws Exception {
        message.setMessage(message.getMessage());
        Thread.sleep(250); // 텀을 주지 않으면, senderName에 받는 사람 값이 들어가는 경우가 있음.
        template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    }
}
