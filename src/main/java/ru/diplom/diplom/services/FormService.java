package ru.diplom.diplom.services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.diplom.models.Form;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.repositories.DirectionRepository;
import ru.diplom.diplom.repositories.FormRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FormService {
    private final FormRepository formRepository;

    public List<Form> getAllForms(){
        return formRepository.findAll();
    }

    @Transactional
    public List<Form> searchForms(String query) {
        List<Form> f;
        f = formRepository.findByNameContainingIgnoreCase(query);
        return f;
    }

    public Optional<Form> getFormById(Integer id) {
        return formRepository.findById(id);
    }

    public Form updateForm(Integer id, Form updatedForm) {
        return formRepository.findById(id).map(f -> {
            f.setName(updatedForm.getName());
            f.setAbbreviation(updatedForm.getAbbreviation());
            return formRepository.save(f);
        }).orElseThrow(() -> new RuntimeException("Форма с id " + id + " не найдена"));
    }

    public Form createForm(Form f) {
        return formRepository.save(f);
    }
}
