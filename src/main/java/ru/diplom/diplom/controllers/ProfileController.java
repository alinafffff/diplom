package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.dto.ProfileDTO;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.dto.UserUpdateCreateDTO;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.models.Profile;
import ru.diplom.diplom.models.User;
import ru.diplom.diplom.services.ProfileService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/profiles")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/all")
    public List<ProfileDTO> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProfileDTO>> searchProfiles(@RequestParam String query) {
        List<ProfileDTO> p = profileService.searchProfiles(query);
        return ResponseEntity.ok(p);
    }

    @GetMapping("/byProfileId")
    public ResponseEntity<Profile> getProfileDTOById(@RequestParam Integer profileId) {
        return profileService.getProfileById(profileId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateProfile/{profileId}")
    public ResponseEntity<String> updateProfile(
            @PathVariable Integer profileId,
            @RequestBody ProfileDTO request) { // Принимаем JSON
        profileService.updateProfile(profileId, request);
        return ResponseEntity.ok("Профиль успешно обновлен");
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createProfile(@RequestBody ProfileDTO profileDTO) {
        profileService.createProfile(profileDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "успешно");

        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(response);
    }

}
