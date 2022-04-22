package jpabook.jpashop.repository;

import jpabook.jpashop.domain.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ChatRoomRepository {

    private final EntityManager em;

    public ChatRoom createChatRoom(String name, Long id1, Long id2){
        ChatRoom chatRoom = ChatRoom.create(name, id1, id2);
        if(chatRoom.getRoomId() == null) {
            em.persist(chatRoom);
        }
        else{
            em.merge(chatRoom);
            em.flush();
        }

        return chatRoom;
    }

    public List<ChatRoom> findAllRooms(){
        return em.createQuery("select c from ChatRoom c", ChatRoom.class)
                .getResultList();
    }

    public ChatRoom findOne(String id){
        return em.find(ChatRoom.class, id);
    }
}
