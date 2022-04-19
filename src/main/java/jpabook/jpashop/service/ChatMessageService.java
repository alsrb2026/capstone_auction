package jpabook.jpashop.service;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;

    public void saveChatMessage(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findAllChatMessages(){
        return chatMessageRepository.findAll();
    }
}
