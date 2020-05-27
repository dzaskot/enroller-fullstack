package com.company.enroller.persistence;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

import javax.persistence.*;

@Entity
@Table(name = "meeting_participant")
public class MeetingParticipant {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@ManyToOne
    @JoinColumn(name = "meeting_id")
	private Meeting meeting;
	
	@ManyToOne
    @JoinColumn(name = "participant_login")
	private Participant participant;
	
	public MeetingParticipant(Meeting meeting, Participant participant) {
		this.meeting = meeting;
		this.participant = participant;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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


}