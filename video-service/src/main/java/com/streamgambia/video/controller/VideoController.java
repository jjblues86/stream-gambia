package com.streamgambia.video.controller;

import com.streamgambia.video.entity.ContentType;
import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoRepository repository;

    // 1. Get All Videos
    @GetMapping
    public ResponseEntity<List<Video>> getAllVideos() {
        return ResponseEntity.ok(repository.findAll());
    }

    // 2. Get Single Video by ID
    @GetMapping("/{id}")
    public ResponseEntity<Video> getVideoById(@PathVariable("id") String id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 3. 👇 NEW: Get Videos by Type (Movie vs Series)
    @GetMapping("/type/{type}")
    public ResponseEntity<List<Video>> getVideosByType(@PathVariable("type") String type) {
        try {
            ContentType contentType = ContentType.valueOf(type.toUpperCase());

            // Note: Ideally we'd use a custom Query in Repository,
            // but for MVP, filtering in Java is fine for small datasets.
            List<Video> filtered = repository.findAll().stream()
                    .filter(v -> v.getType() == contentType)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(filtered);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 4. SEED DATA (Updated with Types)
    @PostMapping("/seed")
    public ResponseEntity<String> seedData() {
        // Clear old data since schema changed
        repository.deleteAll();

        List<Video> movies = List.of(
                // --- MOVIES ---
                Video.builder()
                        .title("The Gambian Dream")
                        .description("A young entrepreneur fights against the odds in Banjul.")
                        .director("Mariama Colley")
                        .genre(Collections.singletonList("Drama"))
                        .type(ContentType.MOVIE) // 👈 Set Type
                        .releaseYear(2024)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                        .build(),
                Video.builder()
                        .title("Banjul Heist")
                        .description("A high-stakes thriller involving a diamond heist.")
                        .director("Ousman Touray")
                        .genre(Collections.singletonList("Action"))
                        .type(ContentType.MOVIE)
                        .releaseYear(2024)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1552072092-7f9b8d63efcb?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
                        .build(),

                // --- SERIES ---
                Video.builder()
                        .title("Tales of the River (Season 1)")
                        .description("An anthology series exploring the myths of the River Gambia.")
                        .director("Samba Jallow")
                        .genre(Collections.singletonList("Drama"))
                        .type(ContentType.SERIES) // 👈 Set Type
                        .releaseYear(2022)
                        .isPremium(false)
                        .thumbnailUrl("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4")
                        .build(),
                Video.builder()
                        .title("Startup Banjul (Season 1)")
                        .description("A comedy series following three friends trying to launch an app.")
                        .director("Musa Mboob")
                        .genre(Collections.singletonList("Comedy"))
                        .type(ContentType.SERIES)
                        .releaseYear(2023)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1515934751635-c81c6bc9a2d8?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4")
                        .build()
        );

        repository.saveAll(movies);
        return ResponseEntity.ok("Database reset and seeded with Movies and Series!");
    }
}