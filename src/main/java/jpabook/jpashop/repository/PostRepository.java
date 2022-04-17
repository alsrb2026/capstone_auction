package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {

    private final EntityManager em;

    @Transactional
    public void save(Post post) {
        if (post.getId() == null) {
            em.persist(post);
        } else {
            em.merge(post);
        }
    }
    @Transactional
    public void delete(Long id) {
        //em.createQuery("delete i from Post i where post_id=:id ");
        Post dPost = em.find(Post.class,id);
        em.remove(dPost);
    }

    public void view(Long id) {
        Post post = em.find(Post.class,id);
        em.createQuery("update Post p SET p.view = p.view + 1 where p.id = :id", Post.class)
                .setParameter("id", id);
    }

    public Post findOne(Long id) {
        return em.find(Post.class, id);
    }

    public List<Post> findAll() {
        return em.createQuery("select i from Post i", Post.class)
                .getResultList();
    }

    public int findAllCnt() {
        return ((Number) em.createQuery("select count(*) from Post")
                .getSingleResult()).intValue();
    }

    public List<Post> findListPaging(int startIndex, int pageSize) {
        return em.createQuery("select b from Post b", Post.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }
}
