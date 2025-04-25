package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.diplom.models.Role;
import ru.diplom.diplom.services.ProfileService;
import ru.diplom.diplom.services.RoleService;

import java.util.List;

@RestController
@RequestMapping("/inpit/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping("/getAllRoles")
    public ResponseEntity<List<Role>> getAllRoles() {
        List<Role> roles = roleService.getAllRoles();
        return ResponseEntity.ok(roles);
    }

}
