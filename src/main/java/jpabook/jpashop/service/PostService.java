package jpabook.jpashop.service;

import jpabook.jpashop.domain.Category;
import jpabook.jpashop.domain.Post;
import jpabook.jpashop.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
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
                           String productName, Category category, int startBid, int winningBid
    , int unitBid, int currentBid, int auctionPeriod, String status, Long currentBidId) {
        Post post = postRepository.findOne(id);
        post.setTitle(title);
        post.setContents(contents);
        post.setProductName(productName);
        post.setCategory(category);
        post.setStartBid(startBid);
        post.setWinningBid(winningBid);
        post.setUnitBid(unitBid);
        post.setCurrentBid(currentBid);
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
    @Modifying
    public void viewPost(Long id) {
        postRepository.view(id);
    }

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }


}


