package jpabook.jpashop.repository;

import jpabook.jpashop.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByName(String name);

    boolean existsByNickname(String nickname);
}