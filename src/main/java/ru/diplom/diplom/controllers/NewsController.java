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

    @GetMapping("/curator/{curatorId}")
    public List<NewsCuratorDTO> getCuratorNews(@PathVariable Integer curatorId) {
        return newsService.getNewsByCuratorId(curatorId);
    }

    @GetMapping("/dekanat/{dId}")
    public List<NewsDTO> getDekanatNews(@PathVariable Integer dId) {
        return newsService.getNewsByDekanatId(dId);
    }

    @PostMapping("/create/{userId}")
    public NewsDTO createNews(@PathVariable Integer userId, @RequestBody NewsDTO newsDTO) {
        return newsService.createNews(userId, newsDTO);
    }

    // Загружаем картинку, получаем путь
    @PostMapping("/uploadImage")
    public ResponseEntity<String> uploadImage(@RequestParam("imageFile") MultipartFile imageFile) {
        String imagePath = newsService.saveImage(imageFile);
        return ResponseEntity.ok(imagePath);
    }

    @PostMapping("/createNewsAsCurator/{myId}")
    public ResponseEntity<News> createNewsFromCurator(@PathVariable Integer myId, @RequestBody NewsCuratorDTO dto) {
        News createdNews = newsService.createNewsAsCurator(myId, dto);
        return ResponseEntity.ok(createdNews);
    }

    @GetMapping("/search/{myId}")
    public ResponseEntity<List<?>> searchNews(@PathVariable Integer myId, @RequestParam String query, @RequestParam String roleName) {
        List<?> n = newsService.searchNews(query, myId, roleName);
        return ResponseEntity.ok(n);
    }

    @GetMapping("/searchDekanatNews/{myId}")
    public ResponseEntity<List<?>> searchDekanatNews(@PathVariable Integer myId, @RequestParam String query, @RequestParam String roleName) {
        List<?> n = newsService.searchDekanatNews(query, myId, roleName);
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

    @PutMapping("/mobile/update/{newsId}")
    public ResponseEntity<News> mobileUpdateNews(@PathVariable Integer newsId, @RequestBody NewsDTO updatedNews) {
        News updated = newsService.mobileUpdateNews(newsId, updatedNews);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/allSudsovetRequests")
    public List<NewsDTO> getAllStudsovetRequests() {
        return newsService.getNewsStudsovetRequests();
    }

    @GetMapping("/allRejectedSudsovetRequests")
    public List<NewsDTO> getAllRejectedStudsovetRequests() {
        return newsService.getNewsStudsovetRejectedRequests();
    }

    @PutMapping("/{newsId}/confirmRequest")
    public ResponseEntity<Void> confirmRequests(@PathVariable Integer newsId) {
        boolean isConfirmed = newsService.confirmStudsovetRequest(newsId);
        if (isConfirmed) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PutMapping("/{newsId}/rejectRequest")
    public ResponseEntity<Void> rejectRequests(@PathVariable Integer newsId) {
        boolean isRejected = newsService.rejectStudsovetRequest(newsId);
        if (isRejected) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
