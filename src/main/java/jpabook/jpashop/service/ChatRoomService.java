package jpabook.jpashop.service;


import jpabook.jpashop.domain.ChatRoom;
import jpabook.jpashop.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    public ChatRoom createChatRoom(String name, Long id1, Long id2, String regisName, String buyerName) {
        return chatRoomRepository.createChatRoom(name, id1, id2, regisName, buyerName);
    }

    public List<ChatRoom> findAllChatRooms(Long id){
        return chatRoomRepository.findAllRooms(id);
    }

    public ChatRoom findChatRoomById(String id){
        return chatRoomRepository.findOne(id);
    }

    // 채팅방 삭제가 아니라 db에서 삭제 말고 사용자에게 안보이도록 권한을 없애는 방식으로 해야함.
    public void exitChatRoom(String roomId, Long id){
        chatRoomRepository.exitRoom(roomId, id);
    }
}

