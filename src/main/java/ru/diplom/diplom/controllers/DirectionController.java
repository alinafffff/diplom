package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.dto.UserUpdateCreateDTO;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.services.DirectionService;
import ru.diplom.diplom.services.ProfileService;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/directions")
public class DirectionController {

    @Autowired
    private DirectionService directionService;

    @GetMapping("/all")
    public List<Direction> getAllDirections() {
        return directionService.getAllDirections();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Direction>> searchDirections(@RequestParam String query) {
        List<Direction> d = directionService.searchDirections(query);
        return ResponseEntity.ok(d);
    }

    @GetMapping("/byDirectionId")
    public ResponseEntity<Direction> getDirectionById(@RequestParam Integer directionId) {
        return directionService.getDirectionById(directionId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateDirection/{id}")
    public ResponseEntity<Direction> updateDirection(
            @PathVariable Integer id,
            @RequestBody Direction updatedDirection) {
        Direction direction = directionService.updateDirection(id, updatedDirection);
        return ResponseEntity.ok(direction);
    }

    @PostMapping("/createDirection")
    public ResponseEntity<String> createDirection(@RequestBody Direction request) {
        directionService.createDirection(request);
        return ResponseEntity.ok("Направление успешно создано");
    }

}
