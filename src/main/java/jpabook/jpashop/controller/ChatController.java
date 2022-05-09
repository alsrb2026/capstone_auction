package jpabook.jpashop.controller;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.domain.ChatRoom;
import jpabook.jpashop.service.ChatMessageService;
import jpabook.jpashop.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
        template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    }
}
