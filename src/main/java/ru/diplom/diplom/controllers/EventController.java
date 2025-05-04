package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.SaveEventDTO;
import ru.diplom.diplom.models.Event;
import ru.diplom.diplom.models.EventType;
import ru.diplom.diplom.services.EventService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/events")
public class EventController {
    @Autowired
    private EventService eventService;

    @GetMapping("/findByCategory")
    public List<Event> findAllByCategory(@RequestParam EventType type) {
        return eventService.findByType(type);
    }

    @GetMapping("/{id}")
    public Event getOne(@PathVariable int id) {
        return eventService.getOne(id);
    }

    @PostMapping
    public Event save(@RequestBody SaveEventDTO saveEventDto) {
        return eventService.save(saveEventDto);
    }

    @DeleteMapping("/deleteById/{id}")
    public Event delete(@PathVariable int id) {
        return eventService.delete(id);
    }

}
