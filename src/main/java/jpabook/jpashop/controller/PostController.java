package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Pagination;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.PostRepository;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;

    @GetMapping("/posts/new")
    public String createForm(HttpServletRequest request, Model model) {
        model.addAttribute("form", new PostForm());
        System.out.println("test="+request.getParameter("category"));
        return "posts/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(PostForm form) {

        Post post = new Post();
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        SimpleDateFormat time = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setCategory(form.getCategory());
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
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        int totalListCnt = postRepository.findAllCnt();
        System.out.println("test totalListCnt =" + totalListCnt);
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        System.out.println("test startIndex =" + startIndex);
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();
        System.out.println("test pageSize =" + pageSize);

        List<Post> boardList = postRepository.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);

//        List<Post> posts = postService.findPosts();
//        model.addAttribute("posts", posts);

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
        form.setCategory(post.getCategory());
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
    public String auctionPost(@RequestParam(defaultValue = "1") int page, @PathVariable Long id, @ModelAttribute("form") PostForm form, Model model) {
//        List<Post> posts = postService.findPosts();
//        model.addAttribute("posts", posts);
        // 총 게시물 수
        int totalListCnt = postRepository.findAllCnt();
        System.out.println("test totalListCnt =" + totalListCnt);
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        System.out.println("test startIndex =" + startIndex);
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();
        System.out.println("test pageSize =" + pageSize);

        List<Post> boardList = postRepository.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);


        postService.updatePost(id, form.getTitle(),
                form.getContents(), form.getProduct_name(), form.getCategory(), form.getStartBid()
                ,form.getWinningBid(), form.getUnitBid(), form.getCurrentBid(), form.getAuctionPeriod(),
                form.getStatus());

        return "redirect:/";
    }


    @GetMapping("post/{id}/delete")
    public String postDelete(@RequestParam(defaultValue = "1") int page, @PathVariable("id") Long id, Model model) {
        Post post = postService.findOne(id);
        //Long deleteId = post.getId();
        postService.deletePost(post.getId());

//        List<Post> posts = postService.findPosts();
//        model.addAttribute("posts", posts);
        int totalListCnt = postRepository.findAllCnt();
        System.out.println("test totalListCnt =" + totalListCnt);
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        System.out.println("test startIndex =" + startIndex);
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();
        System.out.println("test pageSize =" + pageSize);

        List<Post> boardList = postRepository.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);

        return "redirect:/";
    }


    @GetMapping("post/{id}/auction")
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);
        //postService.viewPost(itemId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setCategory(post.getCategory());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setRegisTime(post.getRegisTime());

        model.addAttribute("form", form);
        return "posts/postItemView";
    }

    @PostMapping("/post/{id}/auction")
    public String auctionItem(@ModelAttribute("form")  PostForm form){

        Post post = new Post();
        post.setId(form.getId());
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setCategory(form.getCategory());
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

        return "posts/postItemView";
    }



}
