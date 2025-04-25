package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.dto.ProfileDTO;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.dto.UserUpdateCreateDTO;
import ru.diplom.diplom.models.*;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.ProfileRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final DirectionRepository directionRepository;

    public List<ProfileDTO> getAllProfiles(){
        return profileRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<ProfileDTO> searchProfiles(String query) {
        List<Profile> p;
        p = profileRepository.findByNameContainingIgnoreCase(query);
        return p.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<Profile> getProfileById(Integer profileId) {
        return profileRepository.findById(profileId);
    }
    public void updateProfile(Integer profileId, ProfileDTO dto) {
        Profile p = profileRepository.findById(profileId)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
        p.setName(dto.getName());
        p.setNumber(dto.getNumber());
        profileRepository.save(p);
    }

    public void createProfile(ProfileDTO dto) {
        Profile p = new Profile();
        p.setName(dto.getName());
        p.setNumber(dto.getNumber());

        Direction d = directionRepository.findByAbbreviation(dto.getDirectionAbbreviation())
                .orElseThrow(() -> new RuntimeException("Direction not found with abbreviation: " + dto.getDirectionAbbreviation()));
        p.setDirection(d.getId());
        profileRepository.save(p);
    }


    private ProfileDTO convertToDTO(Profile p) {
        String directionAbbreviation = directionRepository.findById(p.getDirection())
                .map(Direction::getAbbreviation)
                .orElse("Нету такого");

        return new ProfileDTO(p.getId(), p.getName(), p.getNumber(), directionAbbreviation);
    }


}
