package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;
import ru.diplom.diplom.services.EventService;
import ru.diplom.diplom.services.UserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/events")
public class EventController {
    @Autowired
    private EventService eventService;
    @Autowired
    private UserService userService;

    @GetMapping("/{id}")
    public Event getOne(@PathVariable int id) {
        return eventService.getOne(id);
    }

    @PostMapping("/createVolunteering/{authorId}")
    public EventVolunteeringDTO createVolunteering(@PathVariable Integer authorId, @RequestBody EventVolunteeringDTO event) {
        return eventService.createVolunteeringEvent(authorId,event);
    }

    @PostMapping("/createHackathon/{authorId}")
    public EventHackathonDTO createHackathon(@PathVariable Integer authorId, @RequestBody EventHackathonDTO event) {
        return eventService.createHackathonEvent(authorId,event);
    }

    @PostMapping("/createPartnersHackathon/{authorId}")
    public EventPartnersHackathonDTO createPartnersHackathon(@PathVariable Integer authorId, @RequestBody EventPartnersHackathonDTO event) {
        return eventService.createPartnersHackathonEvent(authorId,event);
    }

    @PutMapping("/updateVolunteering/{eventId}")
    public ResponseEntity<EventVolunteeringDTO> updateVolunteering(
            @PathVariable Integer eventId,
            @RequestBody EventVolunteeringDTO dto) {
        EventVolunteeringDTO updatedEvent = eventService.updateVolunteeringEvent(eventId, dto);
        return ResponseEntity.ok(updatedEvent);
    }

    @PutMapping("/updateHackathon/{eventId}")
    public ResponseEntity<EventHackathonDTO> updateHackathon(
            @PathVariable Integer eventId,
            @RequestBody EventHackathonDTO dto) {
        EventHackathonDTO updatedEvent = eventService.updateHackathonEvent(eventId, dto);
        return ResponseEntity.ok(updatedEvent);
    }

    @PutMapping("/updatePartnersHackathon/{eventId}")
    public ResponseEntity<EventPartnersHackathonDTO> updatePartnersHackathon(
            @PathVariable Integer eventId,
            @RequestBody EventPartnersHackathonDTO dto) {
        EventPartnersHackathonDTO updatedEvent = eventService.updatePartnersHackathonEvent(eventId, dto);
        return ResponseEntity.ok(updatedEvent);
    }



    @DeleteMapping("/deleteById/{id}")
    public Event delete(@PathVariable int id) {
        return eventService.delete(id);
    }



    @GetMapping("/allEvents")
    public List<?> getAllEvents() {
        return eventService.getAllEventsWithTypeSpecificDTOs();
    }

    @GetMapping("/allArchiveEvents")
    public List<?> getAllArchiveEvents() {
        return eventService.getAllArchiveEventsWithTypeSpecificDTOs();
    }


    @GetMapping("/findByTypes")
    public List<?> getEventsByTypes(@RequestParam List<String> types) {
        return eventService.getFilteredEventsByTypes(types);
    }

    @GetMapping("/findArchivedByTypes")
    public List<?> getArchivedEventsByTypes(@RequestParam List<String> types) {
        return eventService.getFilteredArchivedEventsByTypes(types);
    }

    @GetMapping("/allStudsovetEvents")
    public List<?> getAllStudsovetEvents() {
        return eventService.getStudentCouncilRequestEvents();
    }
    @GetMapping("/allStudsovetArchivedEvents")
    public List<?> getAllStudsovetArchivedEvents() {
        return eventService.getStudentCouncilRequestArchivedEvents();
    }

    @GetMapping("/findById/{myId}")
    public List<?> getAllMyEvents(@PathVariable Integer myId) {
        return eventService.getAMyEvents(myId);
    }

    @GetMapping("/findAllStudsovetEventsRequests")
    public List<?> getAllStudsovetEventsRequests() {
        return eventService.getEventStudsovetRequests();
    }

    @GetMapping("/findAllStudsovetRejectedEventsRequests")
    public List<?> getAllStudsovetRejectedEventsRequests() {
        return eventService.getEventStudsovetRejectedRequests();
    }

    @GetMapping("/findArchiveById/{myId}")
    public List<?> getAllMyArchivedEvents(@PathVariable Integer myId) {
        return eventService.getAllMyArchivedEvents(myId);
    }

    @GetMapping("/search/{myId}")
    public ResponseEntity<List<?>> searchEvents(@PathVariable Integer myId, @RequestParam String query, @RequestParam String filter) {
        List<?> n = eventService.searchEvents(query, myId, filter);
        return ResponseEntity.ok(n);
    }

    @GetMapping("/searchArchived/{myId}")
    public ResponseEntity<List<?>> searchArchivedEvents(@PathVariable Integer myId, @RequestParam String query, @RequestParam String filter) {
        List<?> n = eventService.searchArchivedEvents(query, myId, filter);
        return ResponseEntity.ok(n);
    }

    @GetMapping("/searchStudsovet")
    public ResponseEntity<?> searchAllStudsovetNewsAndEvents(
            @RequestParam String query,
            @RequestParam String filter) {
        System.out.println("query = " + query + ", filter = " + filter);

        List<?> result = eventService.searchByFilter(query, filter);
        return ResponseEntity.ok(result);
    }


    @PutMapping("/{eventId}/confirmRequest")
    public ResponseEntity<Void> confirmRequests(@PathVariable Integer eventId) {
        boolean isConfirmed = eventService.confirmStudsovetRequest(eventId);
        if (isConfirmed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{eventId}/rejectRequest")
    public ResponseEntity<Void> rejectRequests(@PathVariable Integer eventId) {
        boolean isRejected = eventService.rejectStudsovetRequest(eventId);
        if (isRejected) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
