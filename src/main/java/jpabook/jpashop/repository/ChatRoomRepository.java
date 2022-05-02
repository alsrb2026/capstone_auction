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

    public ChatRoom createChatRoom(String name, Long id1, Long id2, String regisName, String buyerName){
        ChatRoom chatRoom = ChatRoom.create(name, id1, id2, regisName, buyerName);
        if(chatRoom.getRoomId() == null) {
            em.persist(chatRoom);
        }
        else{
            em.merge(chatRoom);
            em.flush();
        }

        return chatRoom;
    }

    public List<ChatRoom> findAllRooms(Long id){
        return em.createQuery("select c from ChatRoom c where c.regisUserId = :id or c.auctionUserId = :id", ChatRoom.class)
                .setParameter("id", id)
                .getResultList();
    }

    public ChatRoom findOne(String id){
        return em.find(ChatRoom.class, id);
    }

    public void exitRoom(String roomId, Long id){

        ChatRoom chatRoom = (ChatRoom) em.createQuery("select c from ChatRoom c where c.roomId = roomId", ChatRoom.class)
                .setParameter("roomId", roomId);
        // 사용자가 나가려고 하는 채팅방을 레포지토리에서 찾아서 auctionUserId인지 regisUserId인지 찾고 해당 필드를 0으로 만들어준다.
        if(chatRoom.getAuctionUserId() == id){
            chatRoom.setAuctionUserId(0L);
        }
        else if(chatRoom.getRegisUserId() == id){
            chatRoom.setRegisUserId(0L);
        }
    }
}
