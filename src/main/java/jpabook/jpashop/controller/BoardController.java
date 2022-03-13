package jpabook.jpashop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class
BoardController {
    @GetMapping("/board")
    public String goBoard(Model model){
        return "boards/mainboard";
    }
}
