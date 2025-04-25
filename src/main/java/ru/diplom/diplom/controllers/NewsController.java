package ru.diplom.diplom.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import ru.diplom.diplom.dto.NewsCuratorDTO;
import ru.diplom.diplom.dto.NewsDTO;
import ru.diplom.diplom.dto.UserAdminDTO;
import ru.diplom.diplom.models.News;
import ru.diplom.diplom.services.NewsService;

import java.util.List;
import java.util.Objects;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/inpit/news")
public class NewsController {
    @Autowired
    private NewsService newsService;

    @GetMapping("/allNews")
    public List<NewsDTO> getAllExceptCurators() {
        return newsService.getAllExceptCurators();
    }

    @GetMapping("/byRole/{roleName}")
    public List<NewsDTO> getByRole(@PathVariable String roleName) {
        return newsService.getNewsByRoleName(roleName);
    }

    @GetMapping("/curator")
    public List<NewsCuratorDTO> getCuratorNews() {
        return newsService.getNewsByCuratorId(8);//костыльб
    }

    @PostMapping("/create")
    public NewsDTO createNews(@RequestBody NewsDTO newsDTO) {
        return newsService.createNews(newsDTO);
    }

    // Загружаем картинку, получаем путь
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile) {
        String imagePath = newsService.saveImage(imageFile);
        return ResponseEntity.ok(imagePath);
    }

    // Создаём новость с указанными параметрами и URL картинки
    @PostMapping("/createNewsAsCurator")
    public ResponseEntity<News> createNewsFromCurator(@RequestBody NewsCuratorDTO dto) {
        News createdNews = newsService.createNewsAsCurator(dto);
        return ResponseEntity.ok(createdNews);
    }

    @GetMapping("/search")
    public ResponseEntity<List<?>> searchNews(@RequestParam String query, @RequestParam String roleName) {
        List<?> n = newsService.searchNews(query, roleName);
        return ResponseEntity.ok(n);
    }

    @DeleteMapping("/delete/{newsId}")
    public ResponseEntity<News> deleteNewsById(@PathVariable Integer newsId){
        newsService.deleteNewById(newsId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{newsId}")
    public ResponseEntity<News> updateNews(@PathVariable Integer newsId, @RequestBody News updatedNews) {
        News updated = newsService.updateNews(newsId, updatedNews);
        return ResponseEntity.ok(updated);
    }


}
