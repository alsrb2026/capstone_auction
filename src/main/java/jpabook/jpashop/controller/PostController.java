package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Pagination;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.PostRepository;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

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

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();
        Long id = userRepository.findByName(name).get().getUserId(); // 상품 등록한 user id 를 repository에서 조회해서 넣었음.

        post.setTitle(form.getTitle());
        post.setPostUserId(id); // 상품 등록한 사용자 id
        post.setContents(form.getContents());
        post.setCategory(form.getCategory());
        post.setProduct_name(form.getProduct_name());
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setCurrentBid(form.getStartBid());
        post.setRegisTime(Timestamp.valueOf(LocalDateTime.now()));
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setStatus("입찰 중");
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
        form.setPostUserId(post.getPostUserId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setCategory(post.getCategory());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(0); // 처음 물품 등록할 때에는 입찰한 사람이 없으므로 현재 입찰 가격은 0으로 한다.
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(0L);
        // 작성 시간
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


    @GetMapping("post/{id}/auction") // id에 해당하는 경매 물품 조회
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();
        Long id = userRepository.findByName(name).get().getUserId();
        //현재 세션에 있는 사용자가 입찰을 해서 이 컨트롤러 함수로 들어온 것이므로 위 코드로 입찰자 id를 획득한다.
        // 아이디 중복이 없다는 가정하에

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setPostUserId(post.getPostUserId());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProduct_name(post.getProduct_name());
        form.setCategory(post.getCategory());
        // 조회 수
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setRegisTime(post.getRegisTime());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(post.getCurrentBidId());

        model.addAttribute("form", form);
        return "posts/postItemView";
    }

    @PostMapping("/post/{id}/auction") // id에 해당하는 물품 입찰.
    public String auctionItem(@ModelAttribute("form")  PostForm form){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();
        Long id = userRepository.findByName(name).get().getUserId(); // 현재 입찰하려고 하는 사용자의 id

        Post post = new Post();
        post.setId(form.getId());
        post.setPostUserId(form.getPostUserId());
        post.setTitle(form.getTitle());
        post.setContents(form.getContents());
        post.setProduct_name(form.getProduct_name());
        post.setCategory(form.getCategory());
        // 조회 수
        post.setStartBid(form.getStartBid());
        post.setWinningBid(form.getWinningBid());
        post.setUnitBid(form.getUnitBid());
        post.setAuctionPeriod(form.getAuctionPeriod());
        post.setRegisTime(form.getRegisTime());

        if(form.getCurrentBid() == 0){ // 현재 입찰하려고하는 사용자가 첫 입찰자인 경우
            post.setCurrentBid(form.getStartBid()); // 시작가격으로 현재 입찰가 설정
            post.setCurrentBidId(id); // 항상 입찰자는 바뀔 수 있으므로, form.get~ 하지 않는다.
            post.setStatus(form.getStatus());
        }
        else{ // 현재 입찰하려고 하는 사용자가 첫 입찰자가 아닌 경우
            if(form.getCurrentBid() == form.getWinningBid()){ // 입찰하려고 하는 금액이 낙찰가일 경우 -> 채팅방도 연결해줘야 함.
                post.setCurrentBidId(id);
                post.setStatus("낙찰됨");
            }
            else if(form.getCurrentBid() < form.getWinningBid()){
                // 입찰하려고 하는 금액이 낙찰가보다 낮은 경우 -> 기간이 지났는지도 확인해야 함. 기간이 지난 경우의
                // 가장 최근에 입찰한 사용자에게 채팅, 낙찰자로 설정해야 함.
                if(!calcDay(form.getAuctionPeriod(), form.getRegisTime())){ // 아직 물품 경매 기간이 지나지 않았을 경우
                    post.setCurrentBid(form.getCurrentBid() + form.getUnitBid());
                    post.setCurrentBidId(id);
                    post.setStatus(form.getStatus());
                }
                else{ // 물품 경매 기간이 지난 경우, 최근에 입찰한 사용자와 물품 등록자 간에 채팅 연결
                    post.setStatus("낙찰됨");
                }
            }
            else{} // 에러 처리, 낙찰가보다 높게 입찰한 경우 이므로
        }

        postService.savePost(post); // service에 transaction=false로 하고, repository에 saveItem에 em.flush()를 해야
        // db에 내용이 반영된다.
        return "posts/postItemView";
    }

    private boolean calcDay(int period, Date regisTime){
        Date now = new Date();
        long gap = now.getTime() - regisTime.getTime();
        long gapHour = gap / (1000 * 60 * 60);
        if(gapHour < period){ // 기간이 지나지 않았을 경우, false
            return false;
        }
        else{ // 기간 지났을 경우, true
            return true;
        }
    }
}

