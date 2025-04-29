package ru.diplom.diplom.services;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.repositories.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private LevelRepository levelRepository;
    @Autowired
    private UserRepository userRepository;

    public void createGroup(GroupCreateDTO dto) {
        Group group = new Group();
        if (dto.getProfileName() != null) {
            Profile profile = profileRepository.findByName(dto.getProfileName())
                    .orElseThrow(() -> new RuntimeException("Профиль не найден: " + dto.getProfileName()));
            group.setProfile(profile.getId());
            group.setDirection(profile.getDirection());
        } else {
            Direction direction = directionRepository.findByAbbreviation(dto.getDirectionAbbreviation())
                    .orElseThrow(() -> new RuntimeException("Направление не найдено: " + dto.getDirectionAbbreviation()));
            group.setDirection(direction.getId());
        }

        if (dto.getFormName() != null) {
            Form form = formRepository.findByName(dto.getFormName())
                    .orElseThrow(() -> new RuntimeException("Форма обучения не найдена: " + dto.getFormName()));
            group.setForm(form.getId());
        }

        if (dto.getLevelName() != null) {
            Level level = levelRepository.findByName(dto.getLevelName())
                    .orElseThrow(() -> new RuntimeException("Уровень не найден: " + dto.getLevelName()));
            group.setMyLevel(level.getId());
        }

        group.setStartDate(dto.getStartDate());
        group.setDuration(dto.getDuration());
        group.setNumber(dto.getNumber());

        groupRepository.save(group);
    }

    public Map<String, List<?>> getGroupsSeparatedByArchiveStatus() {
        List<Group> allGroups = groupRepository.findAll();

        List<GroupAllDTO> activeGroups = new ArrayList<>();
        List<GroupAllArchivedDTO> archivedGroups = new ArrayList<>();

        for (Group g : allGroups) {
            if (isArchived(g.getStartDate(), g.getDuration())) {
                archivedGroups.add(convertToArchivedDTO(g));
            } else {
                activeGroups.add(convertToGroupAllDTO(g));
            }
        }

        Map<String, List<?>> result = new HashMap<>();
        result.put("active", activeGroups);
        result.put("archived", archivedGroups);

        return result;
    }

    public List<GroupAllDTO> getGroupsByDirectionAbbreviation(String abbreviation) {
        return groupRepository.searchByDirectionAbbreviation(abbreviation)
                .stream()
                .map(this::convertToGroupAllDTO)
                .toList();
    }

    public List<GroupAllArchivedDTO> getArchivedGroupsByDirectionAbbreviation(String abbreviation) {
        return groupRepository.searchArchivedByDirectionAbbreviation(abbreviation)
                .stream()
                .map(this::convertToArchivedDTO)
                .toList();
    }

    public void updateActiveGroup(Integer groupId, Group newGroup) {
        Group g = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Group not found"));
        g.setStartDate(newGroup.getStartDate());
        g.setDuration(newGroup.getDuration());
        g.setNumber(newGroup.getNumber());
        groupRepository.save(g);
    }

    public String addCuratorToGroup(Integer groupId, Integer curatorId) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            return "Группа не найдена.";
        }

        // Ситуация 1: У группы уже есть куратор, но он не я
        if (group.getCurator() != null && !group.getCurator().equals(curatorId)) {
            group.setCurator(curatorId);
            groupRepository.save(group);
            return "Группа была успешно передана новому куратору.";
        }

        // Ситуация 2: У группы уже есть куратор — это я, не добавляем
        if (group.getCurator() != null && group.getCurator().equals(curatorId)) {
            return "У этой группы уже есть ваш куратор. Добавление не требуется.";
        }

        // Ситуация 3: У группы нет куратора
        if (group.getCurator() == null) {
            group.setCurator(curatorId);
            groupRepository.save(group);
            return "Группа была успешно добавлена вашему куратору.";
        }

        return "Неизвестная ошибка.";
    }

    // В GroupService
    public String checkCuratorForGroup(Integer groupId, Integer curatorId) {
        Group group = groupRepository.findById(groupId).orElse(null);

        if (group == null) {
            return "Группа не найдена.";
        }

        if (group.getCurator() != null && !group.getCurator().equals(curatorId)) {
            return "У группы уже есть другой куратор.";
        }

        if (group.getCurator() != null && group.getCurator().equals(curatorId)) {
            return "У этой группы уже есть ваш куратор.";
        }

        if (group.getCurator() == null) {
            return "Группа свободна.";
        }

        return "Неизвестная ошибка.";
    }

    public void updateGroupInfo(Integer groupId, Integer starosta, Integer proforg, String newDescription) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        System.out.println("Before update - Description: " + group.getDescription()); // Лог

        if (starosta != null) {
            group.setLeader(starosta);
        }
        if (proforg != null) {
            group.setOrganizer(proforg);
        }
        if (newDescription != null && !newDescription.isEmpty()) {
            group.setDescription(newDescription);
        }

        System.out.println("After update - Description: " + group.getDescription()); // Лог

        Group saved = groupRepository.save(group);
        System.out.println("Saved entity - Description: " + saved.getDescription()); // Лог
    }
    public Optional<Group> getGroupById(Integer groupId) {
        return groupRepository.findById(groupId);
    }
    public List<GroupDTO> getGroupByCuratorId(Integer id) {
        return groupRepository.findByCurator(id)
                .stream()
                .map(this::convertToGroupDTO)
                .toList();
    }

    public List<GroupUpdateDTO> getGroupUpdateByCuratorId(Integer id) {
        return groupRepository.findByCurator(id)
                .stream()
                .map(this::toGroupUpdateDTO)
                .toList();
    }

    public GroupUpdateDTO getGroupUpdateByGroupId(Integer groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new RuntimeException("Группа не найдена"));

        return toGroupUpdateDTO(group);
    }


    public GroupUpdateDTO toGroupUpdateDTO(Group group) {
        User leader = null;
        User organizer = null;

        if (group.getLeader() != null) {
            leader = userRepository.findById(group.getLeader()).orElse(null);
        }
        if (group.getOrganizer() != null) {
            organizer = userRepository.findById(group.getOrganizer()).orElse(null);
        }

        String leaderName = (leader != null)
                ? leader.getSurname() + " " + leader.getName() + " " + leader.getPatronymic()
                : null;

        String organizerName = (organizer != null)
                ? organizer.getSurname() + " " + organizer.getName() + " " + organizer.getPatronymic()
                : null;

        return GroupUpdateDTO.builder()
                .id(group.getId())
                .leaderFullName(leaderName)
                .organizerFullName(organizerName)
                .description(group.getDescription())
                .build();
    }

    private GroupCreateDTO convertToDTO(Group g) {

        String directionAbbreviation = directionRepository.findById(g.getDirection())
                .map(Direction::getAbbreviation)
                .orElse("Not found direction");

        String formName =formRepository.findById(g.getForm())
                .map(Form::getName)
                .orElse("Not found form");

        String levelName =levelRepository.findById(g.getMyLevel())
                .map(Level::getName)
                .orElse("Not found lvl");

        String profileName =profileRepository.findById(g.getProfile())
                .map(Profile::getName)
                .orElse("Not found profile");

        return new GroupCreateDTO(g.getId(), directionAbbreviation, formName, levelName, profileName, g.getStartDate(), g.getDuration(), g.getNumber());
    }

    private GroupAllDTO convertToGroupAllDTO(Group g) {
        String directionAbbreviation;

        if (g.getProfile() != null) {
            directionAbbreviation = profileRepository.findById(g.getProfile())
                    .flatMap(p -> directionRepository.findById(p.getDirection()))
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction from profile");
        } else {
            directionAbbreviation = directionRepository.findById(g.getDirection())
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction");
        }

        String formName = formRepository.findById(g.getForm())
                .map(Form::getName)
                .orElse("Not found form");

        String levelName = levelRepository.findById(g.getMyLevel())
                .map(Level::getName)
                .orElse("Not found level");

        Integer profileNumber = null;
        if (g.getProfile() != null) {
            profileNumber = profileRepository.findById(g.getProfile())
                    .map(Profile::getNumber)
                    .orElse(null);
        }

        int course = calculateCourse(g.getStartDate());
        boolean archived = isArchived(g.getStartDate(), g.getDuration());

        return GroupAllDTO.builder()
                .id(g.getId())
                .directionAbbreviation(directionAbbreviation)
                .formName(formName)
                .levelName(levelName)
                .profileNumber(profileNumber)
                .course(course)
                .number(g.getNumber())
                .archived(archived)
                .build();
    }

    public GroupDTO convertToGroupDTO(Group g) {
        String directionAbbreviation;
        String n;

        if (g.getProfile() != null) {
            directionAbbreviation = profileRepository.findById(g.getProfile())
                    .flatMap(p -> directionRepository.findById(p.getDirection()))
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction from profile");
        } else {
            directionAbbreviation = directionRepository.findById(g.getDirection())
                    .map(Direction::getAbbreviation)
                    .orElse("Not found");
        }

        String formName = formRepository.findById(g.getForm())
                .map(Form::getAbbreviation)
                .orElse("");

        String levelName = levelRepository.findById(g.getMyLevel())
                .map(Level::getAbbreviation)
                .orElse("");

        Integer profileNumber = null;
        if (g.getProfile() != null) {
            profileNumber = profileRepository.findById(g.getProfile())
                    .map(Profile::getNumber)
                    .orElse(null);
        }

        int course = calculateCourse(g.getStartDate());
        boolean archived = isArchived(g.getStartDate(), g.getDuration());

        if (profileNumber == null){
            n = levelName+"-"+directionAbbreviation+formName+"-"+course+g.getNumber();
        }
        else n = levelName+profileNumber+"-"+directionAbbreviation+formName+"-"+course+g.getNumber();

        return GroupDTO.builder()
                .id(g.getId())
                .name(n)
                .build();
    }


    private GroupAllArchivedDTO convertToArchivedDTO(Group g) {
        String directionAbbreviation;

        if (g.getProfile() != null) {
            directionAbbreviation = profileRepository.findById(g.getProfile())
                    .flatMap(p -> directionRepository.findById(p.getDirection()))
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction from profile");
        } else {
            directionAbbreviation = directionRepository.findById(g.getDirection())
                    .map(Direction::getAbbreviation)
                    .orElse("Not found direction");
        }

        String formName = formRepository.findById(g.getForm())
                .map(Form::getName)
                .orElse("Not found form");

        String levelName = levelRepository.findById(g.getMyLevel())
                .map(Level::getName)
                .orElse("Not found level");

        Integer profileNumber = null;
        if (g.getProfile() != null) {
            profileNumber = profileRepository.findById(g.getProfile())
                    .map(Profile::getNumber)
                    .orElse(null);
        }

        Integer endYear = calculateEndYear(g.getStartDate(), g.getDuration());
        boolean archived = isArchived(g.getStartDate(), g.getDuration());

        return GroupAllArchivedDTO.builder()
                .id(g.getId())
                .directionAbbreviation(directionAbbreviation)
                .formName(formName)
                .levelName(levelName)
                .profileNumber(profileNumber)
                .endYear(endYear)
                .number(g.getNumber())
                .archived(archived)
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

    private Integer calculateEndYear(LocalDate startDate, int duration) {
        return startDate.getYear() + duration;
    }


    private boolean isArchived(LocalDate startDate, int duration) {
        LocalDate endOfStudies = startDate.plusYears(duration).withMonth(8).withDayOfMonth(31);
        return LocalDate.now().isAfter(endOfStudies);
    }

}
