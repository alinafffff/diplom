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

    @GetMapping("/forDekanat")
    public List<Notifications> getDekanatNotifications() {
        return notificationsService.getAllNotificationsDekanat();
    }

    @GetMapping("/curator/search")
    public List<Notifications> searchCuratorNotifications(@RequestParam String query) {
        return notificationsService.searchCuratorNotifications(query);
    }

    @GetMapping("/dekanat/search")
    public List<Notifications> searchDekanatNotifications(@RequestParam String query) {
        return notificationsService.searchDekanatNotifications(query);
    }
}
