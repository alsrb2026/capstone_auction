package jpabook.jpashop.controller;

import jpabook.jpashop.Form.PostForm;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.repository.UserRepository;
import jpabook.jpashop.service.*;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ChatRoomService chatRoomService;
    private final FilesService filesService;
    private final PostUserService postUserService;

    @GetMapping("/posts/new")
    public String createForm(HttpServletRequest request, Model model) {
        model.addAttribute("form", new PostForm());

        return "posts/createPostForm";
    }

    @PostMapping("/posts/new")
    public String create(@RequestParam String category, PostForm form, HttpServletRequest request, @RequestPart MultipartFile files) throws Exception{

        Post post = new Post();
        SimpleDateFormat time = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        HttpSession session = request.getSession();
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String name = (String)session.getAttribute("accountId");

        Long id = (Long)session.getAttribute("id"); // 상품 등록한 user id 를 repository에서 조회해서 넣었음.

        String endTime = null;
        Date date = new Date();
        // 포맷변경 ( 년월일 시분초)
        SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        // Java 시간 더하기
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        endTime = sdformat.format(cal.getTime());
        System.out.println("지금 : " + endTime);
        cal.add(Calendar.HOUR, form.getAuctionPeriod()); //시간 추가
        endTime = sdformat.format(cal.getTime());
        System.out.println("마감시간 : " + endTime);

        //시24 넘으면 일1 추가

        post = makePost(post,id,name,form.getTitle(),form.getContents(),form.getProductName(),
                form.getCategory(),0, form.getStartBid(), form.getWinningBid(), form.getUnitBid(),
                form.getStartBid(),Timestamp.valueOf(LocalDateTime.now()), endTime, form.getAuctionPeriod(),
                "입찰 중" ,0L);

        //이미지 업로드 코드
        Files file = new Files();

        String sourceFileName = files.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
        File destinationFile;
        String destinationFileName;
        String path = System.getProperty("user.dir");
        String fileUrl = path+"/photo/";
        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            destinationFile = new File(fileUrl + destinationFileName);
        } while (destinationFile.exists());

        destinationFile.getParentFile().mkdirs();
        files.transferTo(destinationFile);
        file.setFilename(destinationFileName);
        file.setFileOriName(sourceFileName);
        file.setFileurl(fileUrl);
        filesService.save(file);
        post.setFname(file.getFilename());
        postService.savePost(post);



        return "redirect:/";
    }

    @PostMapping("mung/fileinsert")
    public String fileinsert(HttpServletRequest request, @RequestPart MultipartFile files) throws Exception{
        Files file = new Files();

        String sourceFileName = files.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
        File destinationFile;
        String destinationFileName;
        String path = System.getProperty("user.dir");
        String fileUrl = path+"/photo/";
        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            destinationFile = new File(fileUrl + destinationFileName);
        } while (destinationFile.exists());

        destinationFile.getParentFile().mkdirs();
        files.transferTo(destinationFile);

        file.setFilename(destinationFileName);
        file.setFileOriName(sourceFileName);
        file.setFileurl(fileUrl);
        filesService.save(file);
        return "redirect:/mung/insert";
    }

    @GetMapping("/posts")
    public String list(@RequestParam(defaultValue = "1") int page,
                       @RequestParam(name = "order", defaultValue = "newOrder") String order,
                       Model model) {

        System.out.println("www"+order);
        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> boardList = null;

        String link = "posts?order="+order;

        if(order.equals("newOrder")) {
            boardList = postService.findListPaging(startIndex, pageSize);

        } else if(order.equals("deadlineOrder")) {
            boardList = postService.findListDeadLinePaging(startIndex, pageSize);
        }


        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("link",link);
        model.addAttribute("second", "&");
        return "posts/postList";
    }

    @GetMapping("/post/search")
    public String searchList(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                             @RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> searchboardList = postService.findSearchListPaging(startIndex, pageSize, keyword);

        String link = "post/search";

        model.addAttribute("boardList", searchboardList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("link",link);
        model.addAttribute("second", "?");

        return "posts/postList";
    }

    //카테고리 적용 + 검색기능
    @GetMapping("/post/CategoryKeywordSearch")
    public String categoryKeywordSearch(HttpServletRequest request,
                             @RequestParam(value = "keyword") String keyword,
                             @RequestParam(defaultValue = "1") int page, Model model) {


        String category = request.getParameter("category");

        // 총 게시물 수
        int totalListCnt = postService.findAllCategoryKeyword(category, keyword);
        System.out.println("zgzg"+totalListCnt);
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();


        List<Post> searchboardList;
        System.out.println("wow" + category);
        if(category.equals("all")) {
            System.out.println("진입" + category);
            searchboardList = postService.findSearchListPaging(startIndex, pageSize, keyword);
        } else {
            searchboardList = postService.findCategorySearchListPaging(startIndex, pageSize, category, keyword);
        }

        String link = "post/CategoryKeywordSearch?category=" + category + "&keyword=" + keyword;

        model.addAttribute("boardList", searchboardList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("link",link);
        model.addAttribute("second", "&");

        return "posts/postList";
    }

    @GetMapping("/post/searchCategory/{category}") //카테고리 선택
    public String searchCategory(@PathVariable("category") String category, @RequestParam(defaultValue = "1") int page, Model model) {

        // 총 게시물 수
        //int totalListCnt = postService.findAllCount();
        int totalListCnt = postService.findCategoryCount(category);

    // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);
        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        String link = ("post/searchCategory/" + category);
        System.out.println("zzg"+link);

        List<Post> searchboardList = postService.findCategoryListPaging(startIndex, pageSize, category);
        model.addAttribute("boardList", searchboardList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("link", link);
        model.addAttribute("second", "?");

        return "posts/postList";
    }

    @GetMapping("/post/myPost") //내 게시글 목록은 검색창, 페이징 없음
    public String searchMyPost(@RequestParam(defaultValue = "1") int page, Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String nickname = ((UserDetails) principal).getUsername();

        //현재 로그인한 사용자정보 가져와서
        UserEntity myId = userService.findIdByNickname(nickname);
        //사용자의 id값 가져오기
        List<Post> myPosts = postService.findMyListPaging(myId.getUserId());

        model.addAttribute("boardList", myPosts);
        return "posts/myPostList";
    }

    @GetMapping("/post/myBidding") //
    public String myBiddingList(@RequestParam(defaultValue = "1") int page, Model model) {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String nickname = ((UserDetails) principal).getUsername();

        //현재 로그인한 사용자정보 가져와서
        UserEntity myId = userService.findIdByNickname(nickname);
        //사용자의 id값 가져오기
        List<Post> myPosts = postService.findMyListPaging(myId.getUserId());

        model.addAttribute("boardList", myPosts);
        return "posts/myPostList";
    }


    @GetMapping("/post/{id}")
    public String auctionItem(@PathVariable("id") Long id, Model model) {
        Post form = postService.findOne(id);
        model.addAttribute("form", form);
        return "posts/updatePostForm";
    }

    @GetMapping("post/{id}/edit")
    public String auctionPostForm(@PathVariable("id") Long itemId, Model model, HttpServletRequest request) throws Exception {

        System.out.println("yyy"+itemId); //OK
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
        form.setPostUserName(post.getPostUserName());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProductName(post.getProductName());
        form.setCategory(post.getCategory());
        form.setStartBid(post.getStartBid());
        form.setEndTime(post.getEndTime());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(post.getCurrentBidId());
        form.setFname(post.getFname());
        System.out.println("ggg"+post.getFname());
        // 작성 시간
        model.addAttribute("form", form);
        model.addAttribute("id",itemId);
        return "posts/updatePostForm";
    }

    @PostMapping("post/{id}/edit")
    public String auctionPost(@RequestParam(defaultValue = "1") int page, @PathVariable Long id,
                              @ModelAttribute("form") PostForm form, Model model, @RequestPart MultipartFile files) throws Exception {
        // 총 게시물 수
        int totalListCnt = postService.findAllCount();
        // 생성인자로  총 게시물 수, 현재 페이지를 전달
        Pagination pagination = new Pagination(totalListCnt, page);

        //걍 Timestamp(regisTime) -> Long
        // String(endTime) -> Long
        // Long끼리 add 하고 String으로 변환? ㄴ
        //form.getRegisTime() [TimeStamp] 를
        //TimeStamp를 Long으로?
        //html에서 가져오는건 String으로 가져와짐
        //걍 DB에서 TimeStamp형 그대로 가져와서 getTime으로 Long으로 변환해주고 Date.setTime으로
        //최초등록시간 set해주고 Calendar로 변환해준 다음 cal.add로 마감시간 변경

        System.out.println("zzdz" + form.getRegisTime());

        Timestamp regisTime = postService.findRegisTime(id);

        //Service에서 findRegisTime(id)로 특정id값을 가진 게시글의 registime을 가져와서 setTIme에? ㅇㅋ

        String endTime = null;
        Date date = new Date();
        date.setTime(regisTime.getTime());
        // 포맷변경 ( 년월일 시분초)
        SimpleDateFormat sdformat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        // Java 시간 더하기
        Calendar cal = Calendar.getInstance(); //객체 생성
        cal.setTime(date); //Date객체를 Calendar로 변환
        endTime = sdformat.format(cal.getTime());
        System.out.println("최초등록시간 : " + date);
        cal.add(Calendar.HOUR, form.getAuctionPeriod()); //시간 추가
        endTime = sdformat.format(cal.getTime());
        System.out.println("변경된 마감시간 : " + endTime);

        // DB select start index
        int startIndex = pagination.getStartIndex();
        // 페이지 당 보여지는 게시글의 최대 개수
        int pageSize = pagination.getPageSize();

        List<Post> boardList = postService.findListPaging(startIndex, pageSize);

        //이미지 업로드 코드
        Files file = new Files();

        String sourceFileName = files.getOriginalFilename();
        String sourceFileNameExtension = FilenameUtils.getExtension(sourceFileName).toLowerCase();
        File destinationFile;
        String destinationFileName;
        String path = System.getProperty("user.dir");
        String fileUrl = path+"/photo/";
        do {
            destinationFileName = RandomStringUtils.randomAlphanumeric(32) + "." + sourceFileNameExtension;
            destinationFile = new File(fileUrl + destinationFileName);
        } while (destinationFile.exists());

        destinationFile.getParentFile().mkdirs();
        files.transferTo(destinationFile);
        file.setFilename(destinationFileName);
        file.setFileOriName(sourceFileName);
        file.setFileurl(fileUrl);
        filesService.save(file);
        //post.setFname(file.getFilename());

        model.addAttribute("boardList", boardList);
        model.addAttribute("pagination", pagination);

        postService.updatePost(id, form.getTitle(), form.getContents(), form.getProductName(), form.getCategory(),
                form.getStartBid(), form.getWinningBid(), form.getUnitBid(), form.getAuctionPeriod(), endTime, form.getStatus(),
                file.getFilename());

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
    @GetMapping("post/{id}/bid")
    public String auctionItemForm(@PathVariable("id") Long itemId,
                                  Model model, HttpServletRequest request) {
        Post post = postService.findOne(itemId);

        HttpSession session = request.getSession();

        String name = (String)session.getAttribute("accountId"); //현재 로그인 상태의 아이디

        // 현재 세션에 있는 사용자가 입찰을 해서 이 컨트롤러 함수로 들어온 것이므로 위 코드로 입찰자 id를 획득한다.
        // 아이디 중복이 없다는 가정하에

        postService.viewPost(itemId);

        List<PostUser> bidList = postUserService.bidList(itemId);
        int bidCnt = postUserService.bidCount(itemId);

        PostForm form = new PostForm();
        form.setId(post.getId());
        form.setPostUserId(post.getPostUserId());
        form.setPostUserName(post.getPostUserName());
        form.setTitle(post.getTitle());
        form.setContents(post.getContents());
        form.setProductName(post.getProductName());
        form.setCategory(post.getCategory());
        form.setView(post.getView());
        form.setStartBid(post.getStartBid());
        form.setWinningBid(post.getWinningBid());
        form.setUnitBid(post.getUnitBid());
        form.setCurrentBid(post.getCurrentBid());
        form.setRegisTime(post.getRegisTime());
        form.setAuctionPeriod(post.getAuctionPeriod());
        form.setStatus(post.getStatus());
        form.setCurrentBidId(post.getCurrentBidId());
        form.setFname(post.getFname());

        model.addAttribute("form", form);
        model.addAttribute("postUserName",post.getPostUserName());
        model.addAttribute("loginName",name);
        model.addAttribute("file", form.getFname());
        model.addAttribute("bidList", bidList);
        model.addAttribute("bidCnt",bidCnt);

        return "posts/postItemView";
    }

    @ResponseBody
    @Transactional
    @PostMapping("/post/buy") // 즉시 구매이므로 바로 채팅방 연결시켜준다.
    public void buyItem(@RequestParam("postId") Long postId, HttpServletRequest request){

        HttpSession session = request.getSession();
        Long id = (Long)session.getAttribute("id");
        Date date = new Date();
        PostUser postUser = new PostUser();
        // .html 화면에서 이미 즉시 구매하는 사람과 등록한 사람을 구분해서 데이터가 오기 때문에 예외 처리할 필요 X.
        // 현재 구매한 사용자 id, 입찰 상태,
        Post post = postService.findOne(postId);
        postService.updatePostBidStatus(post.getId(), id, post.getWinningBid(), "구매 완료");

        String regisName = userRepository.findById(post.getPostUserId()).get().getNickname();
        String buyerName = (String)session.getAttribute("nickname");

        String bidUserAccountId = (String)session.getAttribute("accountId");
        String postUserAccountId = userRepository.findById(post.getPostUserId()).get().getName();

        postUser.setPostId(postId);
        postUser.setBidUserAccountId(bidUserAccountId);
        postUser.setBidUserName(buyerName);
        postUser.setPostUserAccountId(postUserAccountId);
        postUser.setPostUserName(regisName);
        postUser.setBid(post.getWinningBid());
        postUser.setBidDate(date);
        postUser.setType("즉시 구매");

        postUserService.save(postUser);

        chatRoomService.createChatRoom(post.getProductName(), post.getPostUserId(), id, regisName, buyerName);
    }

    @ResponseBody
    @PostMapping(value = "/post/expired")
    public String btnPressed(@RequestParam("id") Long postId, @RequestParam("postUserId") Long postUserId, @RequestParam("type") String type,
                             HttpServletRequest request){

        Post post = postService.findOne(postId);
        PostUser postUser = new PostUser();
        Date date = new Date();

        HttpSession session = request.getSession();


        if(post.getCurrentBidId() == 0){
            System.out.println("currentBidId = 0, no bid user -> status = '입찰 종료'");
            postService.updatePostStatus(postId, 0L, "입찰 종료");
            return "no bid user";
        }
        else{
            Long currentBidId = post.getCurrentBidId();
            String bidUserAccountId = userRepository.findById(post.getCurrentBidId()).get().getName();
            String bidUserName = userRepository.findById(post.getCurrentBidId()).get().getNickname();
            String postUserAccountId = (String)session.getAttribute("accountId");
            String postUserName = (String)session.getAttribute("nickname");
            System.out.println("currentBidId = " + currentBidId + "has bid user -> status = '낙찰 완료'");

            postService.updatePostStatus(postId, currentBidId, "낙찰 완료");

            postUser.setPostId(postId);
            postUser.setBidUserAccountId(bidUserAccountId);
            postUser.setBidUserName(bidUserName);
            postUser.setPostUserAccountId(postUserAccountId);
            postUser.setPostUserName(postUserName);
            postUser.setBid(post.getCurrentBid());
            postUser.setBidDate(date);
            postUser.setType("낙찰");
            postUserService.save(postUser);

            String productName = postService.findOne(postId).getProductName();
            String regisName = postUserName;

            String buyerName = userRepository.findById(currentBidId).get().getNickname();
            chatRoomService.createChatRoom(productName, postUserId, currentBidId, regisName, buyerName);

            return "has bid user";
        }
    }

    @ResponseBody
    @Transactional
    @PostMapping("/post/bid") // id에 해당하는 물품 입찰.
    public void auctionItem(@RequestParam("postId") Long postId, @RequestParam("regisId") Long regisId, @RequestParam("bid") int bid, HttpServletRequest request) {

        HttpSession session = request.getSession();
        Date date = new Date();
        Long id = (Long)session.getAttribute("id"); // 현재 입찰하려고 하는 사용자의 id

        String bidUserAccountId = (String)session.getAttribute("accountId");
        String postUserAccountId = userRepository.findById(regisId).get().getName();

        PostUser postUser = new PostUser();

        Post form = postService.findOne(postId);

        String regisName = userRepository.findById(form.getPostUserId()).get().getNickname(); // 판매자 닉네임
        String buyerName = (String)session.getAttribute("nickname"); // 구매자 닉네임

        // 1. 경매에 참여할 수 있는지 없는지 부터 체크
        if (!calcDay(form.getAuctionPeriod(), form.getRegisTime())) { // 아직 물품 경매 기간이 지나지 않았을 경우
            // 1-1. 현재 입찰한 사용자가 첫 번째 입찰자일 경우
            if (form.getCurrentBidId() == 0) { //
                System.out.println(">>> startBid , unit" + form.getStartBid() + "   " + form.getUnitBid() + " >>>");
                postService.updatePostBidStatus(form.getId(), id, bid, "입찰 중");
                postUser.setPostId(postId);
                postUser.setBidUserAccountId(bidUserAccountId);
                postUser.setBidUserName(buyerName);
                postUser.setPostUserAccountId(postUserAccountId);
                postUser.setPostUserName(regisName);
                postUser.setBid(bid);
                postUser.setBidDate(date);
                postUser.setType("입찰");
                postUserService.save(postUser);
            }
            // 1-2. 첫 번째 입찰자가 아닐 경우
            else {
                if (form.getCurrentBid() == form.getWinningBid()) { // 1-2-(1). 현재 입찰한 금액이 낙찰가일 경우
                    postService.updatePostBidStatus(form.getId(), id, form.getWinningBid(), "낙찰 완료");
                    postUser.setPostId(postId);
                    postUser.setBidUserAccountId(bidUserAccountId);
                    postUser.setBidUserName(buyerName);
                    postUser.setPostUserAccountId(postUserAccountId);
                    postUser.setPostUserName(regisName);
                    postUser.setBid(form.getWinningBid());
                    postUser.setType("낙찰");
                    postUser.setBidDate(date);
                    postUserService.save(postUser);
                    // 그리고 채팅방 생성, 채팅방 이름 : 물품이름(물품 올린 사용자 닉네임) 이렇게?
                    chatRoomService.createChatRoom(form.getProductName(), form.getPostUserId(), id
                            , regisName, buyerName);
                } else if (form.getCurrentBid() < form.getWinningBid()) { // 1-2-(2). 현재 입찰한 금액이 낙찰가보다 낮을 경우
                    postService.updatePostBidStatus(form.getId(), id, bid, "입찰 중");
                    postUser.setPostId(postId);
                    postUser.setBidUserAccountId(bidUserAccountId);
                    postUser.setBidUserName(buyerName);
                    postUser.setPostUserAccountId(postUserAccountId);
                    postUser.setPostUserName(regisName);
                    postUser.setBid(bid);
                    postUser.setBidDate(date);
                    postUser.setType("입찰");
                    postUserService.save(postUser);
                } else {} // 1-2-(3). 입찰가가 낙찰가보다 큰 경우이므로 에러 처리.
            }
        }
    }

    public Post makePost(Post post, Long id, String name, String title, String contents, String productName,
                         String category, int view, int startBid, int winningBid, int unitBid, int CurrentBid,
                         Timestamp nowTime, String endTime, int auctionPeriod, String status, Long currentBidId){
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
        post.setCurrentBid(startBid); // 처음 물품 등록할 때에는 입찰한 사람이 없으므로 현재 입찰 가격은 시작 가격으로 설정.
        post.setRegisTime(nowTime);
        post.setEndTime(endTime);
        post.setAuctionPeriod(auctionPeriod);
        post.setStatus(status); // 등록하면 바로 입찰 중인 상태가 될 것이기 때문에.
        post.setCurrentBidId(currentBidId);
        post.setView(0);

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


