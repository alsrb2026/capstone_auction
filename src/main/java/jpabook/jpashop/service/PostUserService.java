package jpabook.jpashop.service;


import jpabook.jpashop.domain.PostUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.List;


@Service
@Transactional()
@RequiredArgsConstructor
public class PostUserService {

    private final EntityManager em;

    @Transactional
    public void save(PostUser postUser) {
        if (postUser.getId() == null) {
            em.persist(postUser);
        } else {
            em.merge(postUser);
        }
    }

    @Transactional
    public List<PostUser> bidList(Long id) {
        return em.createQuery("select i from PostUser i where i.postId = :id order by i.bidDate desc", PostUser.class)
                .setParameter("id", id)
                .getResultList();
    }

    @Transactional
    public int bidCount(Long id) {
        return ((Number) em.createQuery("select count(*) from PostUser i where i.postId = :id")
                .setParameter("id", id)
                .getSingleResult()).intValue();
    }




}
