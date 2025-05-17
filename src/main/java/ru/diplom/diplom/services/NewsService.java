package ru.diplom.diplom.services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import ru.diplom.diplom.dto.NewsCuratorDTO;
import ru.diplom.diplom.dto.NewsDTO;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.repositories.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NewsService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final GroupRepository groupRepository;
    private final ProfileRepository profileRepository;
    private final DirectionRepository directionRepository;
    private final FormRepository formRepository;
    private final LevelRepository levelRepository;
    private final NewsRepository newsRepository;
    private final NotificationsService notificationsService;

    public List<NewsDTO> getAllExceptCurators() {
        return newsRepository.findAll().stream()
                .filter(news -> {
                    // 1. Фильтрация по роли (исключаем кураторов)
                    boolean isNotCurator = userRepository.findById(news.getAuthor())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> !role.getName().equalsIgnoreCase("Куратор"))
                            .orElse(true);

                    // 2. Фильтрация по статусу студсовета
                    boolean isApprovedNews = Boolean.FALSE.equals(news.getIsStudentCouncilRequest()) ||
                            (Boolean.TRUE.equals(news.getIsStudentCouncilRequest()) &&
                                    Boolean.FALSE.equals(news.getIsRejected()));

                    return isNotCurator && isApprovedNews;
                })
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }
    public List<NewsDTO> getNewsByRoleName(String roleName) {
        return newsRepository.findAll().stream()
                .filter(news -> {
                    // 1. Фильтрация по роли автора
                    boolean isMatchingRole = userRepository.findById(news.getAuthor())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> role.getName().equalsIgnoreCase(roleName))
                            .orElse(false);

                    // 2. Фильтрация по статусу студсовета
                    boolean isApprovedNews = Boolean.FALSE.equals(news.getIsStudentCouncilRequest()) ||
                            (Boolean.TRUE.equals(news.getIsStudentCouncilRequest()) &&
                                    Boolean.FALSE.equals(news.getIsRejected()));

                    return isMatchingRole && isApprovedNews;
                })
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }

    public List<NewsDTO> getNewsStudsovetRequests() {
        String roleName = "студсовет";
        return newsRepository.findAll().stream()
                .filter(news -> {
                    boolean isMatchingRole = userRepository.findById(news.getAuthor())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> role.getName().equalsIgnoreCase(roleName))
                            .orElse(false);

                    // 2. Фильтрация по статусу студсовета
                    boolean condition = Boolean.TRUE.equals(news.getIsStudentCouncilRequest())
                            && (news.getIsRejected()==null);

                    return isMatchingRole && condition;
                })
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }

    public List<NewsDTO> getNewsStudsovetRejectedRequests() {
        String roleName = "студсовет";
        return newsRepository.findAll().stream()
                .filter(news -> {
                    boolean isMatchingRole = userRepository.findById(news.getAuthor())
                            .flatMap(user -> roleRepository.findById(user.getRole()))
                            .map(role -> role.getName().equalsIgnoreCase(roleName))
                            .orElse(false);

                    // 2. Фильтрация по статусу студсовета
                    boolean condition = Boolean.TRUE.equals(news.getIsStudentCouncilRequest())
                            && Boolean.TRUE.equals(news.getIsRejected());

                    return isMatchingRole && condition;
                })
                .map(this::convertToNewsDTO)
                .collect(Collectors.toList());
    }

    public List<NewsCuratorDTO> getNewsByCuratorId(Integer curatorId) {
        List<News> newsList = newsRepository.findAllByAuthor(curatorId);
        return newsList.stream()
                .map(this::convertToNewsCuratorDTO)
                .toList();
    }

    public List<NewsDTO> getNewsByDekanatId(Integer dId) {
        List<News> newsList = newsRepository.findAllByAuthor(dId);
        return newsList.stream()
                .map(this::convertToNewsDTO)
                .toList();
    }

    public NewsDTO createNews(Integer userId, NewsDTO newsDTO) {
        News news = new News();
        News savedNews = null;

        news.setTitle(newsDTO.getTitle());
        news.setContent(newsDTO.getDescription());
        news.setPhotoUrl(newsDTO.getPhotoUrl());
        news.setCreatedAt(LocalDateTime.now());
        news.setAuthor(userId);

        Optional<User> u = userRepository.findById(userId);
        if(u.get().getRole().equals(5)) {
            news.setIsStudentCouncilRequest(true);
            news.setIsRejected(false);
        }

        savedNews = newsRepository.save(news);
        notificationsService.createNewsNotification(news.getContent());

        return convertToNewsDTO(savedNews);
    }

    public String saveImage(MultipartFile imageFile) {
        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Файл не может быть пустым");
        }

        try {
            String uploadDir = "uploads/images/";
            String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName);
            Files.createDirectories(filePath.getParent());
            Files.write(filePath, imageFile.getBytes());

            return "/uploads/images/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении изображения", e);
        }
    }

    public News createNewsAsCurator(Integer userId, NewsCuratorDTO dto) {
        News news = News.builder()
                .title(dto.getTitle())
                .content(dto.getDescription())
                .photoUrl(dto.getPhotoUrl())
                .createdAt(LocalDateTime.now())
                .myGroup(dto.getGroupId())
                .author(userId)
                .isStudentCouncilRequest(false)
                .isRejected(false)
                .build();

        return newsRepository.save(news);
    }

    public News updateNews(Integer id, News updatedNews) {
        return newsRepository.findById(id)
                .map(existingNews -> {
                    if (updatedNews.getTitle() != null) existingNews.setTitle(updatedNews.getTitle());
                    if (updatedNews.getContent() != null) existingNews.setContent(updatedNews.getContent());
                    existingNews.setPhotoUrl(updatedNews.getPhotoUrl());
                    if (updatedNews.getMyGroup() != null) existingNews.setMyGroup(updatedNews.getMyGroup());

                    return newsRepository.save(existingNews);
                })
                .orElseThrow(() -> new EntityNotFoundException("Новость с id " + id + " не найдена"));
    }

    @Transactional
    public List<?> searchNews(String query, Integer myId, String filter) {
        Role role = roleRepository.findByName(filter);
        List<?> n;

        if ("Все новости".equals(filter)) {

            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> {
                        User author = userRepository.findById(news.getAuthor()).orElse(null);
                        return author != null && !author.getRole().equals(roleRepository.findByName("Куратор").getId());
                    })
                    .map(news -> convertToNewsDTO(news))
                    .collect(Collectors.toList());
        }
        else if ("Мои новости".equals(filter)) {

            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> news.getAuthor().equals(myId))
                    .map(news -> convertToNewsCuratorDTO(news))
                    .collect(Collectors.toList());
        } else {
            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> {
                        User author = userRepository.findById(news.getAuthor()).orElse(null);
                        if (author != null) {
                            return author.getRole().equals(role.getId());
                        }
                        return false;
                    })
                    .map(news -> convertToNewsDTO(news))
                    .collect(Collectors.toList());
        }

        return n;
    }

    public boolean confirmStudsovetRequest(Integer newsId) {
        Optional<News> dto = newsRepository.findById(newsId);
        if (dto.isPresent()) {
            News updatedNews = dto.get();
            updatedNews.setIsRejected(false);
            newsRepository.save(updatedNews);
            return true;
        }
        return false;
    }

    public boolean rejectStudsovetRequest(Integer newsId) {
        Optional<News> dto = newsRepository.findById(newsId);
        if (dto.isPresent()) {
            News updatedNews = dto.get();
            updatedNews.setIsRejected(true);
            newsRepository.save(updatedNews);
            return true;
        }
        return false;
    }
    @Transactional
    public List<?> searchDekanatNews(String query, Integer myId, String filter) {
        Role role = roleRepository.findByName(filter);
        List<?> n;

        if ("Все новости".equals(filter)) {

            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> {
                        User author = userRepository.findById(news.getAuthor()).orElse(null);
                        return author != null && !author.getRole().equals(roleRepository.findByName("Куратор").getId());
                    })
                    .map(news -> convertToNewsDTO(news))
                    .collect(Collectors.toList());
        }
        else if ("Мои новости".equals(filter)) {

            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> news.getAuthor().equals(myId))
                    .map(news -> convertToNewsDTO(news))
                    .collect(Collectors.toList());
        } else {
            n = newsRepository.findByContentContainingIgnoreCase(query)
                    .stream()
                    .filter(news -> {
                        User author = userRepository.findById(news.getAuthor()).orElse(null);
                        if (author != null) {
                            return author.getRole().equals(role.getId());
                        }
                        return false;
                    })
                    .map(news -> convertToNewsDTO(news))
                    .collect(Collectors.toList());
        }

        return n;
    }

    public void deleteNewById(@PathVariable Integer newsId){
        newsRepository.deleteById(newsId);
    }


    private NewsDTO convertToNewsDTO(News news){

        String authorRoleName = userRepository.findById(news.getAuthor())
                .flatMap(user -> roleRepository.findById(user.getRole()))
                .map(Role::getName)
                .orElse("Неизвестная роль");

        return NewsDTO.builder()
                .id(news.getId())
                .createdAt(news.getCreatedAt() != null ? news.getCreatedAt() : LocalDateTime.now())
                .author(news.getAuthor())
                .authorRole(authorRoleName)
                .title(news.getTitle())
                .description(news.getContent())
                .photoUrl(news.getPhotoUrl())
                .build();
    }

    private NewsCuratorDTO convertToNewsCuratorDTO(News news){

        String groupFormatted;
        if (news.getMyGroup() != null) {
            groupFormatted = groupRepository.findById(news.getMyGroup())
                    .map(g -> {

                        String level = levelRepository.findById(g.getMyLevel())
                                .map(Level::getAbbreviation).orElse("ass");

                        String profile = g.getProfile() != null
                                ? profileRepository.findById(g.getProfile())
                                .map(Profile::getNumber)
                                .map(String::valueOf)
                                .orElse("")
                                : "";

                        String direction = g.getProfile() != null
                                ? profileRepository.findById(g.getProfile())
                                .flatMap(p -> directionRepository.findById(p.getDirection()))
                                .map(Direction::getAbbreviation)
                                .orElse("ЭЭЭИ")
                                : directionRepository.findById(g.getDirection())
                                .map(Direction::getAbbreviation)
                                .orElse("ЭЭЭИ2");

                        String form = formRepository.findById(g.getForm())
                                .map(Form::getAbbreviation).orElse("");

                        int course = calculateCourse(g.getStartDate());

                        return String.format("%s%s-%s%s-%d%s",
                                level,
                                profile.isEmpty() ? "" : profile,
                                direction,
                                form.isEmpty() ? "" : form,
                                course,
                                g.getNumber());
                    })
                    .orElse("Группа не найдена");
        } else groupFormatted = null;

        return NewsCuratorDTO.builder()
                .id(news.getId())
                .createdAt(news.getCreatedAt() != null ? news.getCreatedAt() : LocalDateTime.now())
                .author(news.getAuthor())
                .title(news.getTitle())
                .description(news.getContent())
                .photoUrl(news.getPhotoUrl())
                .groupId(news.getMyGroup())
                .group(groupFormatted)
                .build();
    }

    private int calculateCourse(LocalDate startDate) {
        if (startDate == null) return 0;

        LocalDate now = LocalDate.now();
        int course = now.getYear() - startDate.getYear();
        if (now.getMonthValue() < 9) {
            course -= 1;
        }
        return Math.max(course + 1, 1);
    }

    public News mobileUpdateNews(Integer id, NewsDTO updatedNews) {
        return newsRepository.findById(id)
                .map(existingNews -> {
                    if (updatedNews.getTitle() != null) existingNews.setContent(updatedNews.getTitle());
                    if (updatedNews.getDescription() != null) existingNews.setContent(updatedNews.getDescription());
                    if (updatedNews.getPhotoUrl() != null)existingNews.setPhotoUrl(updatedNews.getPhotoUrl());

                    return newsRepository.save(existingNews);
                })
                .orElseThrow(() -> new EntityNotFoundException("Новость с id " + id + " не найдена"));
    }

}
