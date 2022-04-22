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
    public UserEntity findByName(String name) {
        return em.createQuery("select m from user m where m.name = name",
                           UserEntity.class).setParameter("name", name)
               .getSingleResult();
    }

    @Transactional
    public void save(UserEntity user){
        em.persist(user);
    }

}
