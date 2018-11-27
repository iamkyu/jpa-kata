package io.iamkyu;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import javax.persistence.TypedQuery;
import java.util.List;

public class MemberPersistenceTest extends JPAHibernateTest {

    private final String SAVED_MEMBER_ID = "foo";

    @Test
    public void 아이디로_조회() {
        Member member = em.find(Member.class, SAVED_MEMBER_ID);
        assertThat(member).isNotNull();
    }

    @Test
    public void 모두_조회_JPQL() {
        //given when
        TypedQuery<Member> query = em.createQuery("select m from Member m", Member.class);

        //when
        List<Member> members = query.getResultList();

        //then
        assertThat(members.size()).isEqualTo(1);
    }

    @Test
    public void 저장() {
        //given
        String id = SAVED_MEMBER_ID + 1;
        Member member = new Member(id, "world", 99);

        //when
        em.getTransaction().begin();
        em.persist(member);
        em.getTransaction().commit();

        //then
        Member found = em.find(Member.class, id);
        assertThat(found).isEqualTo(member);
    }

    @Test
    public void 삭제() {
        //given
        Member member = em.find(Member.class, SAVED_MEMBER_ID);
        assertThat(member).isNotNull();

        //when
        em.getTransaction().begin();
        em.remove(member);
        em.getTransaction().commit();

        //then
        Member found = em.find(Member.class, SAVED_MEMBER_ID);
        assertThat(found).isNull();
    }

    @Test
    public void 영속_엔티티_동일성_동등성_보장() {
        Member member1 = em.find(Member.class, SAVED_MEMBER_ID);
        Member member2 = em.find(Member.class, SAVED_MEMBER_ID);
        assertThat(member1).isSameAs(member2)
                .isEqualTo(member2);
    }
}
