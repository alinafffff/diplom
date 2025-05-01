package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.Notifications;
import ru.diplom.diplom.services.NotificationsService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/notifications")
public class NotificationsController {
    @Autowired
    private NotificationsService notificationsService;

    @GetMapping("/forСurator")
    public List<Notifications> getCuratorNotifications() {
        return notificationsService.getAllNotificationsCurator();
    }

    @GetMapping("/searchCurator")
    public ResponseEntity<List<Notifications>> searchForCurator(@RequestParam String query) {
        List<Notifications> n = notificationsService.searchCuratorNotifications(query);
        return ResponseEntity.ok(n);
    }

}
