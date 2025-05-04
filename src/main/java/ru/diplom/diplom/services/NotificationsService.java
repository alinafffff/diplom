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
    public void createGroupRequestsNotification(String groupName) {
        createNotification(NotificationType.запрос, "Новый запрос на вступление в группу " + groupName);
    }

    public void createStudsovetRequestsNotification(String title) {
        createNotification(NotificationType.студсовет, "Новый запрос на публикацию от студсовета: " + title);
    }
    public void createResultsNotification() {
        createNotification(NotificationType.результаты, "Новый запрос на подтверждение результатов!");
    }

    public List<Notifications> getAllNotificationsCurator() {
        return notificationRepository.findByTypeInOrderByCreatedAtDesc(
                List.of(NotificationType.новость, NotificationType.система, NotificationType.запрос)
        );
    }

    public List<Notifications> getAllNotificationsDekanat() {
        return notificationRepository.findByTypeInOrderByCreatedAtDesc(
                List.of(NotificationType.новость, NotificationType.система, NotificationType.студсовет, NotificationType.результаты)
        );
    }

    @Transactional
    public List<Notifications> searchCuratorNotifications(String query) {
        return notificationRepository.findByTypeInAndTextContainingIgnoreCase(
                List.of(NotificationType.новость, NotificationType.система, NotificationType.запрос),
                query
        );
    }

    @Transactional
    public List<Notifications> searchDekanatNotifications(String query) {
        return notificationRepository.findByTypeInAndTextContainingIgnoreCase(
                List.of(NotificationType.новость, NotificationType.система,
                        NotificationType.студсовет, NotificationType.результаты),
                query
        );
    }


}
