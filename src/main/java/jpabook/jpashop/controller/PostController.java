package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/posts/new")
    public String createForm(Model model) {
        model.addAttribute("form", new PostForm());
        return "posts/CreatePostForm";
    }

    @PostMapping("/posts/new")
    public String create(PostForm form) {

        Post post = new Post();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setCurrentBid(form.getCurrentBid());
        post.setRegisTime(Timestamp.valueOf(LocalDateTime.now()));
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setStatus(form.getStatus());
        post.setCurrentBidId(form.getCurrentBidId());

        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String list(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "posts/postList";
    }

    @GetMapping("/post/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Post form = postService.findOne(id);
        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @GetMapping("post/{id}/edit")
    public String auctionPostForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());

        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @PostMapping("post/{id}/edit")
    public String auctionPost(@PathVariable Long id, @ModelAttribute("form") PostForm form, Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("posts", posts);

        postService.updatePost(id, form.getTitle(),
                form.getContents(), form.getProduct_name(), form.getStartBid()
                ,form.getWinningBid(), form.getUnitBid(), form.getCurrentBid(), form.getAuctionPeriod(),
                form.getStatus());

        return "posts/postList";
    }


    @GetMapping("post/{id}/delete")
    public String postDelete(@PathVariable("id") Long id, Model model) {
        Post post = postService.findOne(id);
        //Long deleteId = post.getId();
        postService.deletePost(post.getId());
        return "posts/postList";
    }


    @GetMapping("post/{id}/auction")
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setRegisTime(post.getRegisTime());

        model.addAttribute("form", form);
        return "posts/postItemView.html";
    }

    @PostMapping("/post/{id}/auction")
    public String auctionItem(@ModelAttribute("form")  PostForm form){

        Post post = new Post();
        post.setId(form.getId());
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setCurrentBid(form.getCurrentBid());
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setStatus(form.getStatus());
        post.setRegisTime(form.getRegisTime());

        // 낙찰 금액이 최고가와 같을 경우, 해당 금액으로 낙찰한 사용자에게 낙찰시킨다. 상품 id를 주고, 상품의 상태를 낙찰 완료로
//        if(post.getCurrentBid() == post.getWinningBid()){
//            post.setStatus("입찰 완료");
//        }
//
//        if(post.getCurrentBid() < post.getWinningBid()){
//            post.setCurrentBid(post.getCurrentBid() + post.getUnitBid());
//        }

        // 경매 상품 등록 기간이 다 되었을 경우

        postService.savePost(post); // service에 transaction=false로 하고, repository에 saveItem에 em.flush()를 해야
        // db에 내용이 반영된다.

        return "redirect:/";
    }



}
