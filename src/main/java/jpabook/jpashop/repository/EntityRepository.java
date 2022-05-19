package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EntityRepository extends JpaRepository<Post, Long> {
    // Entity 테이블을 id역순으로 정렬한 뒤 첫 로우리턴
// 두메소드의 결과 값은 동일하다.
    Post findFirstByOrderByIdDesc();
    List<Post> findTop5ByOrderByViewDesc();
}

