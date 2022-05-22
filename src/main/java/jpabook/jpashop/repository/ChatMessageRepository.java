package jpabook.jpashop.repository;

import jpabook.jpashop.domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Date;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatMessageRepository {

    private final EntityManager em;

    @Transactional
    public void save(ChatMessage chatMessage){
        if(chatMessage.getId() == null){
            em.persist(chatMessage);
        }
        else{
            em.persist(chatMessage);
            em.flush();
        }
    }

    public List<ChatMessage> findAll(String id){
        return em.createQuery("select cm from ChatMessage cm where cm.chRoomId = :id", ChatMessage.class)
                .setParameter("id", id)
                .getResultList();
    }

    public int findUnReadMsg(String roomId, String recName){
        int count = 0; // 메시지 받는 사용자가 읽지 않은 메시지 개수
        List<ChatMessage> msgList = em.createQuery("select cm from ChatMessage cm where cm.chRoomId = :roomId and cm.receiverName = :recName",
                ChatMessage.class)
                .setParameter("roomId", roomId)
                .setParameter("recName", recName)
                .getResultList();

        for(int i=0;i<msgList.size();i++){
            if(msgList.get(i).getCheckRead() == 0){
                count++;
            }
        }
        return count;
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void updateUnreadMsg(String roomId, Date date) {
        em.createQuery("update ChatMessage cm SET cm.recvTime = :date, cm.checkRead = 1 where cm.chRoomId = :roomId")
                .setParameter("roomId", roomId)
                .setParameter("date", date)
                .executeUpdate();
        em.clear();
    }
}
