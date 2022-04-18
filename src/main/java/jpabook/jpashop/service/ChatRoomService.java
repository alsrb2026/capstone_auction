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

    public ChatRoom createChatRoom(String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    public List<ChatRoom> findAllChatRooms(){
        return chatRoomRepository.findAllRooms();
    }

    public ChatRoom findChatRoomById(String id){
        return chatRoomRepository.findOne(id);
    }

    // 채팅방 삭제
    public void exitChatRoom(String roomId){

    }
}

