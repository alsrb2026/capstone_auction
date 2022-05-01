package jpabook.jpashop.service;

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
                           String productName, String category, int startBid, int winningBid
    , int unitBid, int currentBid, int auctionPeriod, String status ) {
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
    public List<Post> findMyListPaging(int startIndex, int pageSize, Long myId) {
        return postRepository.findMyListPaging(startIndex, pageSize, myId);
    }
}


