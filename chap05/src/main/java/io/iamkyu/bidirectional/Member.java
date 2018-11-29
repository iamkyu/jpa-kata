package io.iamkyu.bidirectional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Member {
    @Id
    @Column(name = "MEMBER_ID")
    private String id;
    private String userName;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private Member() {
    }

    public Member(String id, String userName) {
        this(id, userName, null);
    }

    public Member(String id, String userName, Team team) {
        setId(id);
        setUserName(userName);
        setTeam(team);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (this.id != null) {
            throw new IllegalArgumentException("The id may not be changed.");
        }

        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }
}
