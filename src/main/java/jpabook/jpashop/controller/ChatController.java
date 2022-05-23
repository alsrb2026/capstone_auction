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
        // message.setMessage(message.getMessage());
        // template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        template.convertAndSend("/sub/chat/room", message);
        chatMessageService.saveChatMessage(message);
    }

    @MessageMapping(value = "/item/bid")
    public void auction(Map params) throws Exception {
        template.convertAndSend("/sub/item/bid", params);
    }

    @MessageMapping(value = "/item/buy")
    public void buy(PostForm post) throws Exception {
        template.convertAndSend("/sub/item/buy", post);
    }

    @MessageMapping(value = "/item/expired")
    public void itemExpired(PostForm post) throws Exception {
        template.convertAndSend("/sub/item/expired", post);
    }
}
