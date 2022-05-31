package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Post;
import jpabook.jpashop.domain.PostUser;
import jpabook.jpashop.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.Date;
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

    public List<Post> findManyByStatus(List<Integer> id, String status) {
        return em.createQuery("select i from Post i where i.id IN (:id) AND i.status= :status", Post.class)
                .setParameter("id",id)
                .setParameter("status",status)
                .getResultList();
    }

    public List<Post> findAll() {
        return em.createQuery("select i from Post i order by i.id desc", Post.class)
                .getResultList();
    }

    public int findAllCnt() {
        return ((Number) em.createQuery("select count(*) from Post")
                .getSingleResult()).intValue();
    }

    public List<Post> findListPaging(int startIndex, int pageSize) {
        return em.createQuery("select b from Post b order by b.id desc", Post.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    //마감임박순 리스트
    public List<Post> findListDeadLinePaging(int startIndex, int pageSize) {
        return em.createQuery("select b from Post b where b.status = '입찰 중' order by b.endTime", Post.class)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Post> findSearchListPaging(int startIndex, int pageSize, String keyword) {
        return em.createQuery("select b from Post b where b.title LIKE :keyword order by b.id desc", Post.class)
                .setParameter("keyword","%"+keyword+"%")
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Post> findCategoryListPaging(int startIndex, int pageSize, String category) {
        return em.createQuery("select b from Post b where b.category = :category order by b.id desc", Post.class)
                .setParameter("category",category)
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }

    public List<Post> findCategorySearchListPaging(int startIndex, int pageSize, String category, String keyword) {
        return em.createQuery("select b from Post b where b.category = :category AND b.title LIKE :keyword order by b.id desc", Post.class)
                .setParameter("category",category)
                .setParameter("keyword","%"+keyword+"%")
                .setFirstResult(startIndex)
                .setMaxResults(pageSize)
                .getResultList();
    }


    //내 게시글인지 어떻게 판단할래? JOIN해서? 아니면 id값? -> Post에 postUserId필드가 있음 이거 활용
    public List<Post> findMyListPaging(Long myId) {
        System.out.println("id테스트"+myId);
        return em.createQuery("select b from Post b where b.postUserId = :myId order by b.id desc", Post.class)
                .setParameter("myId",myId)
                .getResultList();
    }

    @Transactional
    @Modifying(clearAutomatically = true)
    public void updatePostStatus(Long id, Long currentBidId, String status) {
        // currentBidId가 0인 경우 -> 물품 등록 기간이 다 지났는데도 입찰가 없는 경우 or 판매자가 마감 버튼을 눌러서 물품 등록을 마감시킬 때,
        // 입찰자가 없는 경우, 있는 경우 status 만 바꿔준다.(입찰 중, 입찰 종료, 낙찰 완료, 구매 완료)
        em.createQuery("update Post p SET p.status = :status where p.id = :id")
                .setParameter("id", id)
                .setParameter("status", status)
                .executeUpdate();
        em.clear();

    }
    @Transactional
    @Modifying(clearAutomatically = true)
    public void updatePostBidStatus(Long id, Long currentBidId, int currentBid, String status) {
        em.createQuery("update Post p SET p.status = :status, p.currentBidId = :currentBidId, p.currentBid = :currentBid where p.id = :id")
                .setParameter("id", id)
                .setParameter("status", status)
                .setParameter("currentBidId", currentBidId)
                .setParameter("currentBid", currentBid)
                .executeUpdate();
        em.clear();
    }

    public int findCategoryCnt(String category) {
        return ((Number) em.createQuery("select count(*) from Post p where p.category = :category")
                .setParameter("category",category)
                .getSingleResult()).intValue();
    }

    public int findCategoryKeywordCnt(String category, String keyword) {
        return ((Number) em.createQuery("select count(*) from Post p where p.category = :category and p.title LIKE :keyword")
                .setParameter("category",category)
                .setParameter("keyword",keyword)
                .getSingleResult()).intValue();
    }


    public Timestamp findRegisTime(Long id) {
        return (Timestamp) em.createQuery("select p.regisTime from Post p where p.id = :id")
                .setParameter("id",id)
                .getSingleResult();
    }

    public UserEntity findIdByNickname(String nickname) {
        return em.createQuery("select m from UserEntity m where m.nickname = :nickname",
                        UserEntity.class).setParameter("nickname", nickname)
                .getSingleResult();
    }

    public List<Integer> findMyBiddingList(String myname) {
        return em.createQuery("select p.postId from PostUser p where p.type = '입찰' AND p.bidUserName = :myname")
                .setParameter("myname", myname)
                .getResultList();
    }
}
