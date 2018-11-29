package io.iamkyu.directional;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Team {
    @Id
    private String id;
    private String name;

    private Team() {
    }

    public Team(String id, String name) {
        setId(id);
        setName(name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
