package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.GroupRequestsDTO;
import ru.diplom.diplom.dto.UserGroupDTO;
import ru.diplom.diplom.models.GroupRequests;
import ru.diplom.diplom.models.News;
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

    @DeleteMapping("/delete/{requestId}")
    public ResponseEntity<GroupRequests> deleteById(@PathVariable Integer requestId){
        groupRequestsService.deleteGroupRequestById(requestId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/approve/{requestId}")
    public ResponseEntity<UserGroupDTO> approveRequest(@PathVariable Integer requestId) {
        UserGroupDTO userGroupDTO = groupRequestsService.approveRequest(requestId);
        return ResponseEntity.ok(userGroupDTO);
    }

    @GetMapping("/search")
    public ResponseEntity<List<GroupRequestsDTO>> searchGroupRequests(
            @RequestParam("query") String query,
            @RequestParam("groupId") Integer groupId
    ) {
        List<GroupRequestsDTO> requests = groupRequestsService.searchRequestsByFullName(query, groupId);
        return ResponseEntity.ok(requests);
    }

}
