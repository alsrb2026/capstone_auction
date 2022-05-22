package jpabook.jpashop.controller;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.domain.ChatRoom;
import jpabook.jpashop.domain.UnreadMsg;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.ChatMessageService;
import jpabook.jpashop.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;

    //채팅방 목록 조회
    @GetMapping(value = "/roomList")
    public ModelAndView rooms(HttpServletRequest request){

        int count = 0; // receiver 가 읽지 않은 메시지 개수.

        ModelAndView mv = new ModelAndView("/roomList");
        HttpSession session = request.getSession();

        Long id = (Long)session.getAttribute("id"); // 현재 접속 중인 사용자 id
        String connectedUserName = (String)session.getAttribute("nickname");
        List<ChatRoom> roomList  = chatRoomService.findAllChatRooms(id);

        // roomList 에 접속할 때, 자신이 채팅 메시지를 읽었는지 아닌지 판단하려면, UnreadMsg라는 클래스를 만든다.
        // 필드로는 방 id, 읽지 않은 메시지를 담을 count 를 만든다. -> 나중에 카카오톡 처럼 상대방이 보낸 마지막 메시지를 저장해서
        // 채팅방 목록에서 마지막 메시지 내용을 확인할 수 있게 할 수 있음.
        // 1. 사용자에게 생성된 채팅방 사이즈 만큼의 배열을 생성한다.
        // 2. 똑같은 사이즈의 UnreadMsg List 를 생성한다.
        // 3. for 문을 돌려서 각 방의 id를 얻고, 현재 접속한 사용자가 그 방에 전달된 메시지의 수신자면, 그 방의 메시지가
        // 몇 개인지 chatMessageService 를 이용해서 count 변수에 넣는다.
        // 4. 아니면 count = 0;
        // 5. 그리고 UnreadMsg msg = new UnreadMsg(방 id, count) 객체 생성해서 List 에 넣는다.
        // 6. for 문을 돌려서 만들어진 List 를 .html 에 데이터로 보낸다.
        // -> 단점 : 실시간성이 떨어짐.

        List<UnreadMsg> unReadMsgList = new ArrayList<>();

        for(int i=0;i<roomList.size(); i++){
            String roomId = roomList.get(i).getRoomId();
            List<ChatMessage> chatList = chatMessageService.findChatMessages(roomId);
            if(chatList.size() != 0 && connectedUserName.equals(chatList.get(chatList.size() - 1).getReceiverName())){
                count = chatMessageService.findUnReadMsg(roomId, connectedUserName);
            }
            else{
                count = 0;
            }
            UnreadMsg msg = new UnreadMsg(roomId, count);
            unReadMsgList.add(msg);
        }

        mv.addObject("list", roomList);
        mv.addObject("unReadMsgList", unReadMsgList);

        return mv;
    }

    //채팅방 조회
    @ResponseBody
    @GetMapping(value = "/room/show")
    public Map<String, Object> getRoom(@RequestParam("roomId") String roomId, Model model, HttpServletRequest request, HttpServletResponse response){

        HttpSession session = request.getSession();
        log.info("# get Chat Room, roomID : " + roomId);

        String name = (String)session.getAttribute("nickname");
        Date now = new Date();
        log.info("# read chat message : " + now);

        ChatRoom chatRoom = chatRoomService.findChatRoomById(roomId);
        List<ChatMessage> chatList = chatMessageService.findChatMessages(roomId);

        // 들어가려는 방의 메시지를 읽었다는 표시로 모든 메시지를 1로 표시해주고, 읽은 시간도 업데이트 해준다.
        if(chatList.size() != 0){
            chatMessageService.updateUnreadMsg(roomId, new Date());
        }

        Map<String, Object> mv = new HashMap<>();

        String user1 = chatRoom.getBuyerName();
        String user2 = chatRoom.getRegisName();

        mv.put("chatList", chatList);
        mv.put("roomId", chatRoom.getRoomId());
        mv.put("user1", user1);
        mv.put("user2", user2);

        return mv;
    }

    // 채팅방 나가는 동시에 리스트에서 삭제
    @PostMapping(value = "/room/exit")
    public String exit(String roomId, HttpServletRequest request){

        HttpSession session = request.getSession();
        Long id = (Long)session.getAttribute("id"); // 채팅방 나가려고 하는 사용자의 id

        chatRoomService.exitChatRoom(roomId, id);

        return "home/home";
    }
}
