package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.GroupRequestsDTO;
import ru.diplom.diplom.dto.UserGroupDTO;
import ru.diplom.diplom.services.GroupRequestsService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/groupRequests")
public class GroupRequestsController {

    @Autowired
    private GroupRequestsService groupRequestsService;

    @GetMapping("/byGroup/{groupId}")
    public ResponseEntity<List<GroupRequestsDTO>> getUsersByGroup(@PathVariable Integer groupId) {
        List<GroupRequestsDTO> users = groupRequestsService.getGroupRequestsByGroupId(groupId);
        return ResponseEntity.ok(users);
    }

}
