package com.company.enroller.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "meeting_participant")
public class MeetingParticipant implements Serializable{

	@EmbeddedId
	private MeetingParticipantPK id;

	public MeetingParticipantPK getId() {
		return id;
	}

	public void setId(MeetingParticipantPK id) {
		this.id = id;
	}





}
