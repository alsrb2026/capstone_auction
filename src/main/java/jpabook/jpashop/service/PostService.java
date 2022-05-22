package jpabook.jpashop.service;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public void savePost(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void updatePost(Long id, String title, String contents,
                           String productName, String category, int startBid,
                           int winningBid, int unitBid, int nextBid,
                           int auctionPeriod, String status, Long currentBidId ) {
        System.out.println("updatett"+id);
        Post post = postRepository.findOne(id);
        post.setTitle(title);
        post.setContents(contents);
        post.setProductName(productName);
        post.setCategory(category);
        post.setStartBid(startBid);
        post.setWinningBid(winningBid);
        post.setUnitBid(unitBid);
        post.setNextBid(nextBid);
        post.setAuctionPeriod(auctionPeriod);
        post.setStatus(status);
        post.setCurrentBidId(currentBidId);
    } // 만약 입찰한 유저가 있고, currentBidId를 update할 때, 같이 넣지 않으면 입찰한 유저 정보가 사라져서 추가함.




    @Transactional // post id, 현재 입찰한 사용자 id, 입찰 상태, 현재 입찰 금액만 업데이트.
    public void auctionPost(Long id, int currentBid, String status, Long currentBidId){
        Post post = postRepository.findOne(id);

    }
    @Transactional
    public void deletePost(Long id) {
        postRepository.delete(id);
    }

    @Transactional
    public void viewPost(Long id) {
        postRepository.view(id);
    }

    //조회수 기준 top5 글 가져오기
//    public List<Post> viewTop5(Long id) {
//        postRepository.viewTop5(id);
//    }

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }

    //게시글 개수
    public int findAllCount() {
        return postRepository.findAllCnt();
    }

    //전체 게시글
    public List<Post> findListPaging(int startIndex, int pageSize){
        return postRepository.findListPaging(startIndex, pageSize);
    }
    //검색 게시글
    public List<Post> findSearchListPaging(int startIndex, int pageSize, String keyword) {
        return postRepository.findSearchListPaging(startIndex, pageSize, keyword);
    }

    //카테고리 별 게시글
    public List<Post> findCategoryListPaging(int startIndex, int pageSize, String keyword) {
        return postRepository.findCategoryListPaging(startIndex, pageSize, keyword);
    }

    //내가 쓴 게시글
    public List<Post> findMyListPaging(Long myId) {
        return postRepository.findMyListPaging(myId);
    }

    public void updatePostStatus(Long id, Long currentBidId, String status){ postRepository.updatePostStatus(id, currentBidId, status); }

    public void updatePostBidStatus(Long id, Long currentBidId, int nextBid, String status){ postRepository.updatePostBidStatus(id, currentBidId, nextBid, status); }
}


