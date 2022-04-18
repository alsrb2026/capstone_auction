package jpabook.jpashop.repository;

import jpabook.jpashop.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final EntityManager em;

    public void save(ChatMessage chatMessage){
        if(chatMessage.getId() == null){
            em.persist(chatMessage);
        }
        else{
            em.persist(chatMessage);
            em.flush();
        }
    }

    public List<ChatMessage> findAll(){
        return em.createQuery("select cm from ChatMessage cm", ChatMessage.class)
                .getResultList();
    }
}
