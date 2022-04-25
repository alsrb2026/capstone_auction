package jpabook.jpashop.controller;

import jpabook.jpashop.service.ChatMessageService;
import jpabook.jpashop.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Log4j2
public class RoomController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    //채팅방 목록 조회
    @GetMapping(value = "/roomlist")
    public ModelAndView rooms(){

        ModelAndView mv = new ModelAndView("/roomlist");

        mv.addObject("list", chatRoomService.findAllChatRooms());

        return mv;
    }

    //채팅방 조회
    @GetMapping("/room")
    public String getRoom(String roomId, Model model){

        log.info("# get Chat Room, roomID : " + roomId);

        model.addAttribute("room", chatRoomService.findChatRoomById(roomId));
        model.addAttribute("chatList", chatMessageService.findChatMessages(roomId));

        return "room";
    }

    // 채팅방 나가는 동시에 리스트에서 삭제
    @PostMapping("/exitRoom")
    public String exit(String roomId){

        chatRoomService.exitChatRoom(roomId);

        return "roomlist";
    }
}
