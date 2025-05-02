package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.NotificationType;
import ru.diplom.diplom.models.Notifications;
import ru.diplom.diplom.repositories.NotificationsRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationsService {
    private final NotificationsRepository notificationRepository;

    public void createNotification(NotificationType type, String text) {
        Notifications notification = Notifications.builder()
                .type(type)
                .text(text)
                .createdAt(LocalDateTime.now())
                .build();

        notificationRepository.save(notification);
    }

    public void createNewsNotification(String newsTitle) {
        createNotification(NotificationType.новость, "Опубликована новость: " + newsTitle);
    }

    public void createCuratorChangeNotification(String groupName) {
        createNotification(NotificationType.система, "В группе " + groupName + " новый куратор");
    }

    public List<Notifications> getAllNotificationsCurator(){ //неверно, у всех будет одинаково, попроси Сашу помочь. может расширить енам! скорее всего
        return notificationRepository.findAllByOrderByCreatedAtDesc();
    }

    @Transactional
    public List<Notifications> searchCuratorNotifications(String query) {
        List<Notifications> n;
        n = notificationRepository.findByTextContainingIgnoreCase(query);
        return n;
    }

}
