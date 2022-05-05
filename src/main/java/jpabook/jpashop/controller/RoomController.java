package jpabook.jpashop.controller;

import jpabook.jpashop.domain.ChatMessage;
import jpabook.jpashop.domain.ChatRoom;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

        String connectedUserName = userRepository.findById(id).get().getNickname();

        List<ChatRoom> roomList  = chatRoomService.findAllChatRooms(id);

        for(int i = 0 ;i < roomList.size() ;i++ ){

        }

        mv.addObject("list", roomList);
        mv.addObject("connectedUserName", connectedUserName);

        return mv;
    }

    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);


        Date now = new Date();
        log.info("# read chat message : " + now);

        List<ChatMessage> chatList = chatMessageService.findChatMessages(roomId);

        for(int i=0;i<chatList.size();i++){
            chatList.get(i).setRecvTime(now);
            chatList.get(i).setCheckRead(1);
        }

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
