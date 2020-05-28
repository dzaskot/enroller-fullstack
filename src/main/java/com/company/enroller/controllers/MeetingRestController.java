package com.company.enroller.controllers;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.MeetingParticipant;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingParticipantService;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/api/meetings")
public class MeetingRestController {

    @Autowired
    MeetingService meetingService;
    @Autowired
    ParticipantService participantService;
    @Autowired
    MeetingParticipantService meetingParticipantService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetings() {
        Collection<Meeting> meetings = meetingService.getAll();
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getMeeting(@PathVariable("id") Long id) {
        Meeting meeting = meetingService.findById(id);
        if (meeting == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(meeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ResponseEntity<?> searchMeeting(@RequestParam(name = "contains") String param){
        List<Meeting> meetings = meetingService.findByTitle(param);
        return new ResponseEntity<>(meetings, HttpStatus.OK);
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<?> registerMeeting(@RequestBody Meeting meeting) {
        Meeting foundMeeting = meetingService.findById(meeting.getId());
        if (foundMeeting != null) {
            return new ResponseEntity<>(
                    "Unable to register. Meeting with id " + foundMeeting.getId() + " already exist",
                    HttpStatus.CONFLICT);
        }

        meetingService.add(meeting);
        return new ResponseEntity<>(meeting, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteMeeting(@PathVariable("id") long id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<>(
                    "Unable to delete. Meeting with id " + id + " doesn't exist",
                    HttpStatus.BAD_REQUEST);
        }

        meetingService.delete(foundMeeting);
        return new ResponseEntity<>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<?> updateMeeting(@PathVariable("id") long id, @RequestBody Meeting updatedMeeting){
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<>(
                    "Unable to update. Meeting with id " + id + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }
        if(updatedMeeting.getTitle() != null) {
            foundMeeting.setTitle(updatedMeeting.getTitle());
        }
        if(updatedMeeting.getDescription() != null) {
            foundMeeting.setDescription(updatedMeeting.getDescription());
        }
        if(updatedMeeting.getDate()!= null) {
            foundMeeting.setDate(updatedMeeting.getDate());
        }

        meetingService.update(foundMeeting);

        return new ResponseEntity<>(foundMeeting, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/{login}", method = RequestMethod.POST)
    public ResponseEntity<?> addMeetingParticipant(@PathVariable("id") long id, @PathVariable("login") String login) {
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

        MeetingParticipant meetingParticipant = new MeetingParticipant(foundMeeting, foundParticipant);
        meetingParticipantService.addMeetingParticipant(meetingParticipant);
        return new ResponseEntity<>(foundParticipant, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/{login}", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteParticipantFromMeeting(@PathVariable("id") Long id, @PathVariable("login") String login) {
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

        meetingParticipantService.deleteFromMeeting(foundParticipant,foundMeeting);
        return new ResponseEntity<>(foundParticipant, HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
    public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
        Meeting foundMeeting = meetingService.findById(id);
        if (foundMeeting == null) {
            return new ResponseEntity<>(
                    "Unable to register. Meeting with id " + id + " doesn't exist",
                    HttpStatus.NOT_FOUND);
        }

        Collection<Participant> participants = meetingService.getParticipants(foundMeeting);
        return new ResponseEntity<>(participants, HttpStatus.OK);
    }

}
