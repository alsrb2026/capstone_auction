package jpabook.jpashop.repository;

import jpabook.jpashop.domain.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class UserRepositoryR {

    private final EntityManager em;

    public UserEntity findIdByNickname(String nickname) {
        return em.createQuery("select m from UserEntity m where m.nickname = :nickname",
                           UserEntity.class).setParameter("nickname", nickname)
               .getSingleResult();
    }

    public UserEntity findById(Long id){
        return em.createQuery("select m from UserEntity m where m.userId = :id",
                        UserEntity.class).setParameter("id", id)
                .getSingleResult();
    }

    public UserEntity findByNickname(String nickname) {
        return em.createQuery("select m from UserEntity m where m.nickname = :nickname", UserEntity.class)
                .setParameter("nickname", nickname)
                .getSingleResult();
    }

    @Transactional
    public void save(UserEntity user){
        em.persist(user);
    }

}
