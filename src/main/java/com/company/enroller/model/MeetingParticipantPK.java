package com.company.enroller.model;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public
class MeetingParticipantPK implements Serializable {

    @ManyToOne
    @JoinColumn(name = "meeting_id")
    private Meeting meeting;

    @ManyToOne
    @JoinColumn(name = "participant_login")
    private Participant participant;

    public MeetingParticipantPK() {
    }

    public MeetingParticipantPK(Meeting meeting, Participant participant) {
        this.meeting = meeting;
        this.participant = participant;
    }

    public Meeting getMeeting() {
        return meeting;
    }

    public void setMeeting(Meeting meeting) {
        this.meeting = meeting;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MeetingParticipantPK)) return false;
        MeetingParticipantPK that = (MeetingParticipantPK) o;
        return Objects.equals(getMeeting(), that.getMeeting()) &&
                Objects.equals(getParticipant(), that.getParticipant());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getMeeting(), getParticipant());
    }
// equals, hashCode
}