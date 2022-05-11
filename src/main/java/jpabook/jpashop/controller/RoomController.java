package jpabook.jpashop.controller;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.domain.ChatRoom;
import jpabook.jpashop.domain.UnreadMsg;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.ChatMessageService;
import jpabook.jpashop.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    @GetMapping("/room")
    public String getRoom(String roomId, Model model, HttpServletRequest request){

        HttpSession session = request.getSession();
        log.info("# get Chat Room, roomID : " + roomId);

        Date now = new Date();
        log.info("# read chat message : " + now);

        List<ChatMessage> chatList = chatMessageService.findChatMessages(roomId);

        for(int i=0;i<chatList.size();i++){
            chatList.get(i).setRecvTime(now);
            chatList.get(i).setCheckRead(1);
        } // 들어가려는 방의 메시지를 읽었다는 표시로 모든 메시지를 1로 표시해주고, 읽은 시간도 업데이트 해준다.

        model.addAttribute("room", chatRoomService.findChatRoomById(roomId));
        model.addAttribute("chatList", chatList);

        return "room";
    }

    // 채팅방 나가는 동시에 리스트에서 삭제
    @PostMapping("/exitRoom")
    public String exit(String roomId){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();
        Long id = userRepository.findByName(name).get().getUserId(); // 채팅방 나가려고 하는 사용자의 id

        chatRoomService.exitChatRoom(roomId, id);

        return "redirect:/";
    }
}
