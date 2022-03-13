package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //스프링이랑 엮어서 실행해라
@SpringBootTest //스프링부트를 띄운상태로 할래 @Autowired이런거 쓰려면 필요
@Transactional //테스트에선 롤백
public class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    javax.persistence.EntityManager em;

    @Test
    @Rollback(false)
    public void 회원가입() throws Exception {
        //given 이런게 주어졌을때
        Member member = new Member();
        member.setName("kim");

        //when 이렇게 하면
        Long saveId = memberService.join(member);

        //then 이렇게 된다(검증해라)
        em.flush();
        assertEquals(member, memberRepository.findOne(saveId)); //1번째랑 2번째 똑같냐

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        memberService.join(member1);
        memberService.join(member2);

        fail("예외가 발생해야 합니다.");


    }
}