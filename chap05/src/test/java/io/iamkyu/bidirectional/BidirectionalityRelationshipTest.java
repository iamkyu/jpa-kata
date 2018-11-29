package io.iamkyu.bidirectional;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class BidirectionalityRelationshipTest {
    private EntityManagerFactory emf;
    private EntityManager em;

    @Before
    public void setUp() {
        emf = Persistence.createEntityManagerFactory("jpabook-test");
        em = emf.createEntityManager();
    }

    @Test
    public void 연관관계_주인이_아닌_곳에_값만_설정하면_반영되지_않는다() {
        //given
        Member member1 = new Member("foo1", "bar1");
        em.persist(member1);
        Member member2 = new Member("foo2", "bar2");
        em.persist(member2);

        Team team1 = new Team("team", "foobar");
        team1.getMembers().add(member1);
        team1.getMembers().add(member2);

        em.persist(team1);

        //when
        Member found = em.find(Member.class, member1.getId());

        //then
        assertThat(found.getTeam()).isNull();
    }

    @Test
    public void 양방향_연관관계_저장() {
        //given
        Team team1 = new Team("team", "foobar");
        em.persist(team1);

        Member member1 = new Member("foo1", "bar1");
        member1.setTeam(team1);
        team1.getMembers().add(member1);
        em.persist(member1);

        //when
        Member found = em.find(Member.class, member1.getId());

        //then
        assertThat(found.getTeam()).isNotNull();
    }
}
