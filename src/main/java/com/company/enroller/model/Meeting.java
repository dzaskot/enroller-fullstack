package com.company.enroller.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "meeting")
public class Meeting {

    private Set<Participant> participants = new HashSet<>();
    private long id;
    private String title;
    private String description;
    private String date;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long getId() {
        return id;
    }

    @Column
    public String getTitle() {
        return title;
    }

    @Column
    public String getDescription() {
        return description;
    }

    @Column
    public String getDate() {
        return date;
    }

//    @JsonIgnore
    @ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name = "meeting_participant", joinColumns = {
            @JoinColumn(name = "participant_login")}, inverseJoinColumns = {@JoinColumn(name = "meeting_id")})
    public Set<Participant> getParticipants() {
        return this.participants;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
    }

    public void removeParticipant(Participant participant) {
        this.participants.remove(participant);
    }

    public void setParticipants(Set<Participant> participants) {
        this.participants = participants;
    }
}
