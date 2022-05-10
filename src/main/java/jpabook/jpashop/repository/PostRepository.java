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

    public List<Post> findSearchListPaging(int startIndex, int pageSize, String keyword) {
        return em.createQuery("select b from Post b where b.title LIKE :keyword", Post.class)
                .setParameter("keyword","%"+keyword+"%")
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Post> findCategoryListPaging(int startIndex, int pageSize, String keyword) {
        return em.createQuery("select b from Post b where b.category = :keyword", Post.class)
                .setParameter("keyword",keyword)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }
//내 게시글인지 어떻게 판단할래? JOIN해서? 아니면 id값? -> Post에 postUserId필드가 있음 이거 활용
    public List<Post> findMyListPaging(Long myId) {
        System.out.println("id테스트"+myId);
        return em.createQuery("select b from Post b where b.postUserId = :myId", Post.class)
                .setParameter("myId",myId)
                .getResultList();
    }

    public void viewTop5(Long id) {
        //return em.createQuery("select b from Post b order by view desc limit 5", Post.class)
    }
}
