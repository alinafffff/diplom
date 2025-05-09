package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.TeamEventDTO;
import ru.diplom.diplom.dto.UserEventShortDTO;
import ru.diplom.diplom.models.Team;
import ru.diplom.diplom.services.TeamUserService;

import java.util.List;

@RestController
@RequestMapping("/inpit/teamUsers")
@CrossOrigin(origins = "*")

public class TeamUserController {
    @Autowired
    private TeamUserService teamUserService;

    @GetMapping("/{studentId}/events")
    public ResponseEntity<List<UserEventShortDTO>> getUserEvents(@PathVariable Integer studentId) {
        List<UserEventShortDTO> events = teamUserService.getEventsByStudentId(studentId);
        return ResponseEntity.ok(events);
    }

    @GetMapping("/event/{eventId}/teams")
    public ResponseEntity<List<?>> getUsersByEvent(@PathVariable Integer eventId) {
        return ResponseEntity.ok(teamUserService.getUsersByEventId(eventId));
    }

    @DeleteMapping("/removeStudentFromEvent/{eventId}/user/{userId}")
    public ResponseEntity<Void> removeStudentFromEvent(
            @PathVariable Integer eventId,
            @PathVariable Integer userId
    ) {
        teamUserService.removeUserFromEvent(userId, eventId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/partnerHackathons")
    public ResponseEntity<List<TeamEventDTO>> getPartnerHackathonTeams() {
        return ResponseEntity.ok(teamUserService.getAllPartnerHackathonTeams());
    }

    @PutMapping("/{teamId}/confirm")
    public ResponseEntity<TeamEventDTO> confirmTeam(@PathVariable Integer teamId) {
        TeamEventDTO result = teamUserService.confirmTeam(teamId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/{teamId}/reject")
    public ResponseEntity<TeamEventDTO> rejectTeam(@PathVariable Integer teamId) {
        Team team = teamUserService.rejectTeam(teamId);
        return ResponseEntity.ok(teamUserService.convertToTeamEventDTO(team));
    }

    @GetMapping("/getAllConfirmedPartnerHackathons")
    public ResponseEntity<List<TeamEventDTO>> getConfirmedPartnerHackathons() {
        List<TeamEventDTO> teams = teamUserService.getAllConfirmedPartnerHackathons();
        return ResponseEntity.ok(teams);
    }


}
