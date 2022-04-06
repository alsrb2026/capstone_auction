package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    public void save(Post post) {
        if (post.getId() == null) {
            em.persist(post);
        } else {
            em.merge(post);
        }
    }

    public void delete(Long id) {
        //em.createQuery("delete i from Post i where post_id=:id ");
        Post dPost = em.find(Post.class,id);
        em.remove(dPost);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select i from Post i", Post.class)
                .getResultList();
    }
}
