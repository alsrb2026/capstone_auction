package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
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
        Post dPost = em.find(Post.class,id);
        em.remove(dPost);
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void view(Long id) {
        em.createQuery(
                        "update Post p SET p.view = p.view + 1 where p.id= :id")
                .setParameter("id", id)
                .executeUpdate();
        em.clear();
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

    @Transactional
    @Modifying(clearAutomatically = true)
    public void updatePostStatus(Long id, Long currentBidId, String status) {
        // currentBidId가 0인 경우 -> 물품 등록 기간이 다 지났는데도 입찰가 없는 경우 or 판매자가 마감 버튼을 눌러서 물품 등록을 마감시킬 때,
        // 입찰자가 없는 경우
        if(currentBidId == 0){
            em.createQuery("update Post p SET p.status = :status where p.id = :id")
                    .setParameter("id", id)
                    .setParameter("status", status)
                    .executeUpdate();
            em.clear();
        }
        // 나머지는 입찰자, 낙찰자 즉시 구매자 or 마감 시간 지나고 최고가로 입찰한 유저가 있는 경우.
        // post/auction, post/buy 둘 다 postRepository로 직접 접근해서 동작하므로 수정해야 함.
        else{

        }

    }
}
