package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.*;
import ru.diplom.diplom.models.Group;
import ru.diplom.diplom.models.Profile;
import ru.diplom.diplom.services.GroupService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping("/create")
    public ResponseEntity<String> createGroup(@RequestBody GroupCreateDTO dto) {
        try {
            groupService.createGroup(dto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Группа успешно создана");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ошибка: " + e.getMessage());
        }
    }

    @GetMapping(value = "/separated", produces = "application/json; charset=UTF-8")
    public ResponseEntity<Map<String, List<?>>> getGroupsSeparatedByArchiveStatus() {
        Map<String, List<?>> groupsMap = groupService.getGroupsSeparatedByArchiveStatus();
        return ResponseEntity.ok(groupsMap);
    }

    @GetMapping(value = "/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<GroupAllDTO>> getGroupsByAbbreviation(
            @RequestParam String abbreviation) {
        return ResponseEntity.ok(groupService.getGroupsByDirectionAbbreviation(abbreviation));
    }

    @GetMapping(value = "/archived/search", produces = "application/json; charset=UTF-8")
    public ResponseEntity<List<GroupAllArchivedDTO>> getArchivedGroupsByAbbreviation(
            @RequestParam String abbreviation) {
        return ResponseEntity.ok(groupService.getArchivedGroupsByDirectionAbbreviation(abbreviation));
    }

    @GetMapping("/byGroupId")
    public ResponseEntity<Group> getGroupById(@RequestParam Integer groupId) {
        return groupService.getGroupById(groupId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/byCuratorId/{curatorId}")
    public List<GroupDTO> getGroupsForDropdown(@PathVariable Integer curatorId) {
        return groupService.getGroupByCuratorId(curatorId);
    }


    @PutMapping("/updateGroup/{groupId}")
    public ResponseEntity<String> updateGroup(
            @PathVariable Integer groupId,
            @RequestBody Group request) { // Принимаем JSON
        groupService.updateActiveGroup(groupId, request);
        return ResponseEntity.ok("Группа успешно обновлена");
    }


}
