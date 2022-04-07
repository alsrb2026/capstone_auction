package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Post;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final PostService postService;

    @GetMapping("/itemList")
    public String showItemList(Model model) {
        List<Post> posts = postService.findPosts();

        model.addAttribute("posts", posts);
        return "/posts/showItemList";
    }


}
