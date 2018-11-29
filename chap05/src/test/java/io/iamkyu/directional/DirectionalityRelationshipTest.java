package io.iamkyu.directional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.RollbackException;
import java.util.List;

public class DirectionalityRelationshipTest {
    private EntityManagerFactory emf;
    private EntityManager em;
    private Team team1 = new Team("team", "foobar");
    private Member member1 = new Member("foo1", "bar1", team1);
    private Member member2 = new Member("foo2", "bar2", team1);

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("jpabook-test");
        em = emf.createEntityManager();

        em.getTransaction().begin();
        em.persist(team1);
        em.persist(member1);
        em.persist(member2);
        em.getTransaction().commit();
    }

    @After
    public void after() {
        em.getTransaction().begin();
        em.remove(member1);
        em.remove(member2);
        em.remove(team1);
        em.getTransaction().commit();

        em.clear();
        em.close();
        emf.close();
    }

    @Test
    public void 연관관계() {
        Team teamOfMember1 = em.find(Member.class, member1.getId()).getTeam();
        Team teamOfMember2 = em.find(Member.class, member2.getId()).getTeam();
        assertThat(teamOfMember1).isEqualTo(team1).isSameAs(team1);
        assertThat(teamOfMember2).isEqualTo(team1).isSameAs(team1);
    }

    @Test
    public void JPQL_을_통한_조인() {
        //given
        String jpql = "select m from Member m  join m.team t where t.name =: teamName";

        //then
        List<Member> memberOfTeam = em.createQuery(jpql, Member.class)
                .setParameter("teamName", team1.getName())
                .getResultList();

        //then
        assertThat(memberOfTeam.size()).isEqualTo(2);
    }

    @Test
    public void 연관관계_수정() {
        //given
        em.getTransaction().begin();
        Member before = new Member("fooo", "baar", team1);
        em.persist(before);
        em.getTransaction().commit();

        em.getTransaction().begin();
        Team newTeam = new Team("newteam", "helloworld");
        em.persist(newTeam);
        em.getTransaction().commit();

        //when
        before.setTeam(newTeam);

        //then
        Member after = em.find(Member.class, before.getId());
        assertThat(after.getTeam()).isEqualTo(newTeam);
    }

    @Ignore("TODO 뭔가 문제로 제대로 동작안함 원인 파악 필요")
    @Test(expected = RollbackException.class)
    public void 연관된_엔티티가_있는데_삭제_시도() {
        em.getTransaction().begin();
        em.remove(team1);
        em.getTransaction().commit();

        fail("연관 된 엔티티를 제거하지 않고 삭제를 시도하면 외래 키 제약조건에 따라 예외가 발생해야 한다.");
    }
}
