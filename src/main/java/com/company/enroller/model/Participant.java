package com.company.enroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "participant")
public class Participant {

    private Set<Meeting> meetings = new HashSet<Meeting>(0);
    private String login;
    private String password;

    @Id
    public String getLogin() {
        return login;
    }

    @Column
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    public String getPassword() {
        return password;
    }

    @JsonIgnore
    @ManyToMany(mappedBy = "participants", fetch=FetchType.EAGER)
    public Set<Meeting> getMeetings() {
        return this.meetings;
    }

    public void setLogin(String login) {
        this.login = login;
    }



    public void setPassword(String password) {
        this.password = password;
    }

    public void setMeetings(Set<Meeting> meetings) {
        this.meetings = meetings;
    }
}
