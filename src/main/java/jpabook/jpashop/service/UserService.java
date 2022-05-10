package jpabook.jpashop.service;

import jpabook.jpashop.domain.UserEntity;
import jpabook.jpashop.repository.UserRepositoryR;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = false)
@RequiredArgsConstructor
public class UserService {

    private final UserRepositoryR userRepositoryR;

    @Transactional
    public UserEntity findIdByNickname(String nickname) {
        return userRepositoryR.findIdByNickname(nickname);
    }

}


