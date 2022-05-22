package jpabook.jpashop.service;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveChatMessage(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }
    // DB에 채팅이 채팅 고유의 id와 채팅이 일어나는 방 번호가 저장되어 있으므로, 채팅방 번호에 해당하는 채팅들을 list로 받아온다.
    public List<ChatMessage> findChatMessages(String id){
        return chatMessageRepository.findAll(id);
    }
    public int findUnReadMsg(String roomId, String recName) { return chatMessageRepository.findUnReadMsg(roomId, recName); }

    public void updateUnreadMsg(String roomId, Date date, String name){ chatMessageRepository.updateUnreadMsg(roomId, date, name); }
}
