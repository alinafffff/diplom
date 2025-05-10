package ru.diplom.diplom.sheduledTasks;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.diplom.diplom.repositories.*;
import ru.diplom.diplom.models.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class VolunteerPointsScheduler {

    private final EventRepository eventRepository;
    private final TeamRepository teamRepository;
    private final TeamUserRepository teamMyUserRepository;
    private final UserRepository userRepository;

    @Scheduled(cron = "0 0 18 * * *")
    @Transactional
    public void awardVolunteerPoints() {
        LocalDateTime now = LocalDateTime.now();

        List<Event> finishedVolunteerEvents = eventRepository.findAll().stream()
                .filter(event ->
                        event.getType().getName().equalsIgnoreCase("волонтерство") &&
                                hasVolunteerEventFinished(event, now) &&
                                (
                                        Boolean.FALSE.equals(event.getIsStudentCouncilRequest()) ||
                                                (Boolean.TRUE.equals(event.getIsStudentCouncilRequest()) &&
                                                        Boolean.FALSE.equals(event.getIsRejected()))
                                )
                )
                .toList();

        for (Event event : finishedVolunteerEvents) {
            List<Integer> teamIds = teamRepository.findAllByMyEvent(event.getId())
                    .stream()
                    .map(Team::getId)
                    .toList();


            for (Integer teamId : teamIds) {
                List<User> users = teamMyUserRepository.findUsersByTeamId(teamId);
                for (User user : users) {
                    int currentPoints = user.getPoints() != null ? user.getPoints() : 0;
                    int newPoints = currentPoints + (event.getPoints() != null ? event.getPoints() : 0);
                    user.setPoints(newPoints);
                    userRepository.save(user);
                }
            }

        }
    }

    private boolean hasVolunteerEventFinished(Event event, LocalDateTime now) {
        LocalDateTime startOfYesterday = now.minusDays(1).toLocalDate().atStartOfDay();
        LocalDateTime endOfYesterday = startOfYesterday.plusDays(1).minusSeconds(1);

        if (event.getEndDate() != null) {
            return !event.getEndDate().isBefore(startOfYesterday) && !event.getEndDate().isAfter(endOfYesterday);
        } else {
            LocalDateTime assumedEndDate = event.getStartDate().plusHours(1);
            return !assumedEndDate.isBefore(startOfYesterday) && !assumedEndDate.isAfter(endOfYesterday);
        }
    }

}

