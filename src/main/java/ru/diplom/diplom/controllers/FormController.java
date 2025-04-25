package ru.diplom.diplom.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.diplom.models.Form;
import ru.diplom.diplom.models.Level;
import ru.diplom.diplom.services.FormService;
import ru.diplom.diplom.services.LevelService;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/forms")
public class FormController {

    @Autowired
    private FormService formService;

    @GetMapping("/all")
    public List<Form> getAllForms() {
        return formService.getAllForms();
    }

    @GetMapping("/search")
    public ResponseEntity<List<Form>> searchForms(@RequestParam String query) {
        List<Form> f = formService.searchForms(query);
        return ResponseEntity.ok(f);
    }

    @GetMapping("/byFormId")
    public ResponseEntity<Form> getFormById(@RequestParam Integer formId) {
        return formService.getFormById(formId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/updateForm/{id}")
    public ResponseEntity<Form> updateForm(
            @PathVariable Integer id,
            @RequestBody Form updatedForm) {
        Form f = formService.updateForm(id, updatedForm);
        return ResponseEntity.ok(f);
    }

    @PostMapping("/createForm")
    public ResponseEntity<String> createForm(@RequestBody Form request) {
        formService.createForm(request);
        return ResponseEntity.ok("Форма успешно создана");
    }

}
