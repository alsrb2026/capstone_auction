package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/itemList")
    public String showItemList(Model model) {
        List<Item> items = itemService.findItems();

        model.addAttribute("items", items);
        return "/posts/showItemList";
    }


}
