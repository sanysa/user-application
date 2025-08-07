package com.bitlab.project.controller;

import com.bitlab.project.model.Chapter;
import com.bitlab.project.service.ChapterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chapters")
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @PostMapping
    public ResponseEntity<Chapter> createChapter(@RequestBody ChapterController chapter) {
        return ResponseEntity.ok(chapterService.createChapter(chapter));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Chapter> getChapter(@PathVariable Long id) {
        return ResponseEntity.ok(chapterService.getChapter(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Chapter> updateChapter(@PathVariable Long id, @RequestBody Chapter chapter) {
        return ResponseEntity.ok(chapterService.updateChapter(id, chapter));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChapter(@PathVariable Long id) {
        chapterService.deleteChapter(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<Chapter>> getAllChapters() {
        return ResponseEntity.ok(chapterService.getAllChapters());
    }
}
