package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.models.Direction;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.services.DirectionService;
import ru.diplom.diplom.services.LevelService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/levels")
public class LevelController {

    @Autowired
    private LevelService levelService;

    @GetMapping("/all")
    public List<Level> getAllLevels() {
        return levelService.getAllLevels();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Level>> searchLevels(@RequestParam String query) {
        List<Level> l = levelService.searchLevels(query);
        return ResponseEntity.ok(l);
    }

    @GetMapping("/byLevelId")
    public ResponseEntity<Level> getLevelById(@RequestParam Integer levelId) {
        return levelService.getLevelById(levelId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateLevel/{id}")
    public ResponseEntity<Level> updateLevel(
            @PathVariable Integer id,
            @RequestBody Level updatedLevel) {
        Level level = levelService.updateLevel(id, updatedLevel);
        return ResponseEntity.ok(level);
    }

    @PostMapping("/createLevel")
    public ResponseEntity<String> createLevel(@RequestBody Level request) {
        levelService.createLevel(request);
        return ResponseEntity.ok("Уровень успешно создан");
    }

}