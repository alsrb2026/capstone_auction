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
                           String product_name, int startBid, int winningBid
    , int unitBid, int currentBid, int auctionPeriod, String status ) {
        Post post = postRepository.findOne(id);
        post.setTitle(title);
        post.setContents(contents);
        post.setProduct_name(product_name);
        post.setStartBid(startBid);
        post.setWinningBid(winningBid);
        post.setUnitBid(unitBid);
        post.setCurrentBid(currentBid);
        post.setAuctionPeriod(auctionPeriod);
        post.setStatus(status);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.delete(id);
    }

    public List<Post> findPosts() {
        return postRepository.findAll();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }
}
