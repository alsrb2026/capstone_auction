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

    public void saveItem(Post post) {
        postRepository.save(post);
    }

    public List<Post> findItems() {
        return postRepository.findAll();
    }

    public Post findOne(Long id) {
        return postRepository.findOne(id);
    }
}
