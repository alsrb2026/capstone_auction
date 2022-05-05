package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Pagination;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.PostRepository;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.ChatRoomService;
import jpabook.jpashop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
    private final ChatRoomService chatRoomService;

    @GetMapping("/posts/new")
    public String createForm(HttpServletRequest request, Model model) {
        model.addAttribute("form", new PostForm());
        return "posts/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(@RequestParam String category, PostForm form) {

        Post post = new Post();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();

        Long id = userRepository.findByName(name).get().getUserId(); // 상품 등록한 user id 를 repository에서 조회해서 넣었음.

        post = makePost(post,id,name,form.getTitle(),form.getContents(),form.getProductName(),
                form.getCategory(),0, form.getStartBid(), form.getWinningBid(), form.getUnitBid(),
                form.getStartBid(),Timestamp.valueOf(LocalDateTime.now()),form.getAuctionPeriod(),
                "입찰 중" ,0L);

        postService.savePost(post);
        return "redirect:/";
    }

    @GetMapping("/posts")
    public String list(@RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> boardList = postService.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);


        return "posts/postList";
    }

    @GetMapping("/post/search")
    public String searchList(@RequestParam(value = "keyword") String keyword, @RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> searchboardList = postService.findSearchListPaging(startIndex, pageSize, keyword);

        model.addAttribute("boardList", searchboardList);
        model.addAttribute("pagination", pagination);

        return "posts/postList";
    }

    @GetMapping("/post/searchCategory/{keyword}")
    public String searchCategory(@PathVariable("keyword") String keyword, @RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> searchboardList = postService.findCategoryListPaging(startIndex, pageSize, keyword);

        model.addAttribute("boardList", searchboardList);
        model.addAttribute("pagination", pagination);

        return "posts/postList";
    }

    @GetMapping("/post/myPost") //내 게시글 목록은 검색창, 페이징 없음
    public String searchMyPost(Model model) {
        List<Post> posts = postService.findPosts();
        model.addAttribute("boardList", posts);
        return "posts/myPostList";
    }


    @GetMapping("/post/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Post form = postService.findOne(id);
        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @GetMapping("post/{id}/edit")
    public String auctionPostForm(@PathVariable("id") Long itemId, Model model, HttpServletRequest request) {

        PostForm form = new PostForm();
        Post post = postService.findOne(itemId);
        HttpSession session = request.getSession();
        //현재 로그인 한 사용자 아이디 name에 저장

        String name = (String)session.getAttribute("accountId"); // 05-04. 세션에서 접속 사용자 아이디 불러오는 것으로 수정.

        //자신의 게시글이 아니면 수정,삭제 버튼이 안보이게 해놨지만 만약을 대비해 서버에서 한번 더 체크
        //로그인 한 사용자 name과 글 작성자 name과 다르면 수정 못하게 이전페이지로 보내기

        if(!name.equals(post.getPostUserName())) {
            return "redirect:/";
        }

        form.setId(post.getId());
        form.setPostUserId(post.getPostUserId());
        form.setPostUserName(form.getPostUserName());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProductName(post.getProductName());
        form.setCategory(post.getCategory());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setNextBid(post.getNextBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(post.getCurrentBidId());
        // 작성 시간
        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @PostMapping("post/{id}/edit")
    public String auctionPost(@RequestParam(defaultValue = "1") int page, @PathVariable Long id, @ModelAttribute("form") PostForm form, Model model) {
//        List<Post> posts = postService.findPosts();
//        model.addAttribute("posts", posts);
        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> boardList = postService.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);


        postService.updatePost(id, form.getTitle(),
                form.getContents(), form.getProductName(), form.getCategory(), form.getStartBid()
                , form.getWinningBid(), form.getUnitBid(), form.getNextBid(), form.getAuctionPeriod(),
                form.getStatus(), form.getCurrentBidId());

        return "redirect:/";
    }


    @GetMapping("post/{id}/delete")
    public String postDelete(@RequestParam(defaultValue = "1") int page, @PathVariable("id") Long id, Model model) {

        Post post = postService.findOne(id);

        //현재 로그인 한 사용자 아이디 name에 저장
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername();

        //자신의 게시글이 아니면 수정,삭제 버튼이 안보이게 해놨지만 만약을 대비해 서버에서 한번 더 체크
        //로그인 한 사용자 name과 글 작성자 name과 다르면 수정 못하게 이전페이지로 보내기
        if(!name.equals(post.getPostUserName())) {
            return "redirect:/";
        }

        postService.deletePost(post.getId());

        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> boardList = postService.findListPaging(startIndex, pageSize);

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);

        return "redirect:/";
    }

    // id에 해당하는 경매 물품 조회
    @GetMapping("post/{id}/auction")
    public String auctionItemForm(@PathVariable("id") Long itemId, Model model) {
        Post post = postService.findOne(itemId);

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = ((UserDetails) principal).getUsername(); //현재 로그인 상태의 아이디
        Long id = userRepository.findByName(name).get().getUserId();
        // 현재 세션에 있는 사용자가 입찰을 해서 이 컨트롤러 함수로 들어온 것이므로 위 코드로 입찰자 id를 획득한다.
        // 아이디 중복이 없다는 가정하에

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setPostUserId(post.getPostUserId());
        form.setPostUserName(form.getPostUserName());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProductName(post.getProductName());
        form.setCategory(post.getCategory());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setNextBid(post.getNextBid());
        form.setRegisTime(post.getRegisTime());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(post.getCurrentBidId());

        model.addAttribute("form", form);
        model.addAttribute("postUserName",post.getPostUserName());
        model.addAttribute("loginName",name);
        return "posts/postItemView";
    }

    @Transactional
    @PostMapping("/auction/buy") // 즉시 구매이므로 바로 채팅방 연결시켜준다.
    public String buyItem(@ModelAttribute("form") PostForm form, HttpServletRequest request, Model model){

        HttpSession session = request.getSession();
        Long id = (Long)session.getAttribute("id");
        // .html 화면에서 이미 즉시 구매하는 사람과 등록한 사람을 구분해서 데이터가 오기 때문에 예외 처리할 필요 X.
        // 현재 구매한 사용자 id, 입찰 상태,

        Post post = postRepository.findOne(form.getId());
        post.setCurrentBidId(id);
        post.setNextBid(post.getWinningBid());
        post.setStatus("구매 완료");

        System.out.println(post.getPostUserName());

        String regisName = userRepository.findByName(post.getPostUserName()).get().getNickname();
        String buyerName = (String)session.getAttribute("nickname");

        chatRoomService.createChatRoom(post.getProductName() + "()", post.getPostUserId(), id, regisName, buyerName);

        model.addAttribute("list", chatRoomService.findAllChatRooms(id));
        model.addAttribute("connectedUserName", buyerName);

        return "/roomList";
    }

    @Transactional
    @PostMapping("/post/{id}/auction") // id에 해당하는 물품 입찰.
    public String auctionItem(@RequestParam(defaultValue = "1") int page, HttpServletRequest request,
                              @ModelAttribute("form")  @PathVariable("id") Long regisIdPostForm, PostForm form,Model model) {

        HttpSession session = request.getSession();

        Long id = (Long)session.getAttribute("id"); // 현재 입찰하려고 하는 사용자의 id

        String regisName = userRepository.findById(form.getPostUserId()).get().getNickname(); // 판매자 닉네임
        String buyerName = userRepository.findById(id).get().getNickname(); // 구매자 닉네임

        Post post = postRepository.findOne(form.getId());

        // 1. 경매에 참여할 수 있는지 없는지 부터 체크
        if (!calcDay(form.getAuctionPeriod(), form.getRegisTime())) { // 아직 물품 경매 기간이 지나지 않았을 경우
            // 1-1. 현재 입찰한 사용자가 첫 번째 입찰자일 경우
            if (form.getCurrentBidId() == 0) { //
                post.setCurrentBidId(id);
                post.setStatus("입찰 중");
                post.setNextBid(form.getStartBid() + form.getUnitBid());
            }
            // 1-2. 첫 번째 입찰자가 아닐 경우
            else {
                if (form.getNextBid() == form.getWinningBid()) { // 1-2-(1). 현재 입찰한 금액이 낙찰가일 경우
                    post.setCurrentBidId(id);
                    post.setStatus("낙찰됨");
                    // 그리고 채팅방 생성, 채팅방 이름 : 물품이름(물품 올린 사용자 닉네임) 이렇게?

                    chatRoomService.createChatRoom(form.getProductName() + "()", form.getPostUserId(), id
                    , regisName, buyerName);

                    model.addAttribute("list", chatRoomService.findAllChatRooms(id));
                    model.addAttribute("connectedUserName", buyerName);

                    return "/roomlist";
                } else if (form.getNextBid() < form.getWinningBid()) { // 1-2-(2). 현재 입찰한 금액이 낙찰가보다 낮을 경우
                    post.setCurrentBidId(id);
                    post.setStatus("입찰 중");
                    post.setNextBid(form.getNextBid() + form.getUnitBid());
                } else {
                } // 1-2-(3). 입찰가가 낙찰가보다 큰 경우이므로 에러 처리.
            }
        }
        // 2. 경매 기간이 지난 경우.
        else{
            // 2-1. 물품에 입찰자가 있는지 체크
            if(post.getCurrentBidId() != 0){ // 2.2 현재 입찰 id 값을 0으로 초기화했으므로 0이 아닌 경우 -> 입찰자가 존재하는 경우
                post.setCurrentBidId(id);
                post.setStatus("낙찰됨");
                // 그리고 채팅 연결.

            }
            else{
                post.setStatus("입찰 종료"); // 낙찰자 없이 종료
            }
        }
        
        // 입찰 중(초기 상태), 낙찰됨(낙찰될 경우), 입찰 종료(시간 지나고 입찰자가 없을 경우) 이 3가지가 입찰 상태
        
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

        return "posts/postList";
    }

    public Post makePost(Post post, Long id, String name, String title, String contents, String productName,
                   String category, int view, int startBid, int winningBid, int unitBid, int nextBid,
                   Timestamp nowTime, int auctionPeriod, String status, Long currentBidId ){
        post.setPostUserId(id); // 상품 등록한 사용자 id
        post.setPostUserName(name); // 상품 등록한 사용자 id(닉네임)
        post.setTitle(title);
        post.setContents(contents);
        post.setProductName(productName);
        post.setCategory(category);
        post.setView(view); // 조회 수도 초기 값은 0으로
        post.setStartBid(startBid);
        post.setWinningBid(winningBid);
        post.setUnitBid(unitBid);
        post.setNextBid(startBid); // 처음 물품 등록할 때에는 입찰한 사람이 없으므로 현재 입찰 가격은 시작 가격으로 설정.
        post.setRegisTime(nowTime);
        post.setAuctionPeriod(auctionPeriod);
        post.setStatus(status); // 등록하면 바로 입찰 중인 상태가 될 것이기 때문에.
        post.setCurrentBidId(currentBidId);

        return post;
    }

    private boolean calcDay(int period, Date regisTime) {
        Date now = new Date();
        long gap = now.getTime() - regisTime.getTime();
        long gapHour = gap / (1000 * 60 * 60);
        if (gapHour < period) { // 기간이 지나지 않았을 경우, false
            return false;
        } else { // 기간 지났을 경우, true
            return true;
        }
    }
}


