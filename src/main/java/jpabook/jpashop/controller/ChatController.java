package jpabook.jpashop.controller;

import jpabook.jpashop.Form.PostForm;
import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import netscape.javascript.JSObject;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final SimpMessagingTemplate template;

    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessage message) throws Exception {
        List<ChatMessage> list = chatMessageService.findChatMessages(message.getChRoomId());
        if(list.size() == 0){
            message.setCheckRead(1);
            chatMessageService.saveChatMessage(message);
            template.convertAndSend("/sub/chat/room", message);
        }

    }

    @MessageMapping(value = "/chat/exit")
    public void exit(ChatMessage message) throws Exception {
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
