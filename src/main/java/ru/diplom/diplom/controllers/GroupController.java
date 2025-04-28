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


    @PostMapping("/addCurator/{groupId}/{curatorId}")
    public ResponseEntity<String> addCuratorToGroup(@PathVariable Integer groupId, @PathVariable Integer curatorId) {
        String result = groupService.addCuratorToGroup(groupId, curatorId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/checkCurator/{groupId}/{curatorId}")
    public ResponseEntity<String> checkCuratorForGroup(@PathVariable Integer groupId, @PathVariable Integer curatorId) {
        String result = groupService.checkCuratorForGroup(groupId, curatorId);
        return ResponseEntity.ok(result);
    }

//    @PutMapping("/updateInfo/{groupId}")
//    public ResponseEntity<String> updateGroupInfo(@PathVariable Integer groupId, @RequestBody Group g) {
//        try {
//            groupService.updateGroupInfo(groupId, g);
//            return ResponseEntity.ok("Информация о группе успешно обновлена.");
//        } catch (RuntimeException e) {
//            return ResponseEntity.badRequest().body(e.getMessage());
//        }
//    }

    @GetMapping("/getInfoByGroup/{groupId}")
    public ResponseEntity<GroupUpdateDTO> getGroupInfoByGroupId(@PathVariable Integer groupId) {
        try {
            GroupUpdateDTO group = groupService.getGroupUpdateByGroupId(groupId);
            return ResponseEntity.ok(group);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }
    @PutMapping("/updateInfo/{groupId}")
    public ResponseEntity<String> updateLeaders(@PathVariable Integer groupId, @RequestParam(name = "starosta",required = false) Integer starosta, @RequestParam(name = "proforg",required = false) Integer proforg){
        groupService.updateGroupInfo(groupId, starosta, proforg);
        return ResponseEntity.ok("Группа успешно обновлена");
    }

}
