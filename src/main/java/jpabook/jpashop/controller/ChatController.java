package jpabook.jpashop.controller;

import jpabook.jpashop.Form.PostForm;
import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessage message) throws Exception {
        // template.convertAndSend("/sub/chat/room" + message.getChRoomId(), message);
        template.convertAndSend("/sub/chat/room", message);
        chatMessageService.saveChatMessage(message);
    }

    @MessageMapping(value = "/chat/message")
    public void message(ChatMessage message) throws Exception {
        template.convertAndSend("/sub/chat/room", message);
        System.out.println(message.getChRoomId());
        if(message.getCheckRead() != -1){
            chatMessageService.saveChatMessage(message);
        }
    }
}
