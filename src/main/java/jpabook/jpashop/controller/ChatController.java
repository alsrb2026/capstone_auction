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
    /*
    @MessageMapping(value = "/chat/enter")
    public void enter(ChatMessage message) throws Exception {
        template.convertAndSend("/sub/chat/room/" + message.getChRoomId(), message);
        chatMessageService.saveChatMessage(message);
    } // -> enter를 하면 채팅방 들어갈때마다 입장했다는 메시지가 전송돼서 빼기로 함. 접속시간으로 어떻게 해보면 될 것 같기도 함.
    */

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
