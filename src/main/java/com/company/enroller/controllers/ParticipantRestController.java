package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.MeetingParticipantPK;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingParticipantService;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@RestController
@RequestMapping("/api/participants")
public class ParticipantRestController {

    @Autowired
    ParticipantService participantService;
    @Autowired
    MeetingParticipantService meetingParticipantService;
    @Autowired
    MeetingService meetingService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAll() {
        Collection<Participant> participants = participantService.getAll();
        return new ResponseEntity<Collection<Participant>>(participants, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> get(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Participant>(participant, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipant(@RequestBody Participant participant) {
        if (participantService.findByLogin(participant.getLogin()) != null) {
            return new ResponseEntity(
                    "Unable to create. A participant with login " + participant.getLogin() + " already exist.",
                    HttpStatus.CONFLICT);
        }
        participantService.add(participant);
        return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable("id") String login) {
        Participant participant = participantService.findByLogin(login);
        if (participant == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        participantService.delete(participant);
        return new ResponseEntity<Participant>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable("id") String login,
                                    @RequestBody Participant updatedParticipant) {
        if (participantService.findByLogin(login) != null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        updatedParticipant.setLogin(login); // in case of login!=updatedParticipant.getLogin()
        participantService.update(updatedParticipant);
        return new ResponseEntity<Participant>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/{login}/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> addParticipantToMeeting(@PathVariable("id") long id, @PathVariable("login") String login) {
        Meeting foundMeeting = meetingService.findById(id);
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundMeeting == null) {
            return new ResponseEntity<>(
                    "Unable to register. Meeting with id " + id + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }
        if (foundParticipant == null) {
            return new ResponseEntity<>(
                    "Unable to register. Participant with login " + login + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }

        foundMeeting.getParticipants().add(foundParticipant);
        MeetingParticipant meetingParticipant = new MeetingParticipant();
        meetingParticipant.setId(new MeetingParticipantPK(foundMeeting,foundParticipant));
        meetingParticipantService.addMeetingParticipant(meetingParticipant);
        return new ResponseEntity<>(foundParticipant, HttpStatus.OK);
    }

    @RequestMapping(value = "/{login}/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> removeMeetingParticipant(@PathVariable("id") Long id, @PathVariable("login") String login) {
        Meeting foundMeeting = meetingService.findById(id);
        Participant foundParticipant = participantService.findByLogin(login);
        if (foundMeeting == null) {
            return new ResponseEntity<>(
                    "Unable to delete. Meeting with id " + id + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }
        if (foundParticipant == null) {
            return new ResponseEntity<>(
                    "Unable to register. Participant with login: " + login + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }

        foundMeeting.getParticipants().remove(foundParticipant);
        meetingParticipantService.deleteFromMeeting(foundMeeting, foundParticipant);
        return new ResponseEntity<>(foundParticipant, HttpStatus.OK);
    }
}
