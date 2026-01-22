package com.streamgambia.video.controller;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
import com.streamgambia.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:3001")
@RequiredArgsConstructor
public class VideoController {

    private final VideoRepository repository;
    private final VideoService videoService;

    // ✅ CORRECT: Accepts Metadata + File, and maps to the correct URL
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Keeps URL as POST /videos
    @ResponseStatus(HttpStatus.CREATED)
    public Video uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("director") String director
    ) throws IOException, InterruptedException {
        // Passes ALL data to the service so it saves to Postgres correctly
        return videoService.uploadVideo(file, title, description, director);
    }
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }

    @GetMapping("/{id}")
    public Video getVideoById(@PathVariable("id") String id) {
        return videoService.getVideoById(id);
    }

    // 2. SEED DATA (This is the missing piece!)
    @PostMapping("/seed")
    public ResponseEntity<String> seedData() {
        if (repository.count() > 0) {
            return ResponseEntity.ok("Database already has movies!");
        }

        List<Video> movies = List.of(
                // --- DRAMA ---
                Video.builder()
                        .title("The Gambian Dream")
                        .description("A young entrepreneur fights against the odds in Banjul to build a tech empire that changes the region forever.")
                        .director("Mariama Colley")
                        .genre("Drama")
                        .releaseYear(2024)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                        .build(),
                Video.builder()
                        .title("River Gambia's Secret")
                        .description("A fisherman discovers an ancient artifact that could rewrite the history of West Africa.")
                        .director("Samba Jallow")
                        .genre("Drama")
                        .releaseYear(2022)
                        .isPremium(false)
                        .thumbnailUrl("https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/TearsOfSteel.mp4")
                        .build(),

                // --- ROMANCE ---
                Video.builder()
                        .title("Smiling Coast Sunset")
                        .description("Two tourists from different worlds meet at Kotu Beach and find a connection that transcends borders.")
                        .director("Ebrima Ceesay")
                        .genre("Romance")
                        .releaseYear(2023)
                        .isPremium(false)
                        .thumbnailUrl("https://images.unsplash.com/photo-1510076857177-7470076d4098?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ElephantsDream.mp4")
                        .build(),
                Video.builder()
                        .title("Love in Serrekunda")
                        .description("A vibrant wedding celebration is threatened by a family feud, but love finds a way.")
                        .director("Aminata Sow")
                        .genre("Romance")
                        .releaseYear(2025)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1515934751635-c81c6bc9a2d8?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/Sintel.mp4")
                        .build(),

                // --- ACTION ---
                Video.builder()
                        .title("Wolof Warriors")
                        .description("An epic historical retelling of the Jolly empire's rise and the warriors who defended it.")
                        .director("Modou Joof")
                        .genre("Action")
                        .releaseYear(2025)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1598899134739-9609c961130c?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerBlazes.mp4")
                        .build(),
                Video.builder()
                        .title("Banjul Heist")
                        .description("A high-stakes thriller involving a diamond heist at the port of Banjul.")
                        .director("Ousman Touray")
                        .genre("Action")
                        .releaseYear(2024)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1552072092-7f9b8d63efcb?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerEscapes.mp4")
                        .build(),

                // --- DOCUMENTARY ---
                Video.builder()
                        .title("Taste of Benachin")
                        .description("A culinary journey through the markets of The Gambia, exploring the secrets of the perfect Jollof Rice.")
                        .director("Fatou Bensouda")
                        .genre("Documentary")
                        .releaseYear(2021)
                        .isPremium(false)
                        .thumbnailUrl("https://images.unsplash.com/photo-1604382354936-07c5d9983bd3?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerJoyrides.mp4")
                        .build(),
                Video.builder()
                        .title("Wild Gambia")
                        .description("Explore the rich biodiversity of the River Gambia National Park, from hippos to chimpanzees.")
                        .director("David Attenborough (Narrator)")
                        .genre("Documentary")
                        .releaseYear(2020)
                        .isPremium(true)
                        .thumbnailUrl("https://images.unsplash.com/photo-1535941339077-2dd1c7963098?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/ForBiggerMeltdowns.mp4")
                        .build(),

                // --- COMEDY ---
                Video.builder()
                        .title("Taxi to nowhere")
                        .description("A hilarious ride with a taxi driver who knows everyone in town but can't find his way home.")
                        .director("Musa Mboob")
                        .genre("Comedy")
                        .releaseYear(2023)
                        .isPremium(false)
                        .thumbnailUrl("https://images.unsplash.com/photo-1559828754-072049e6234b?auto=format&fit=crop&w=800&q=80")
                        .videoUrl("http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/VolkswagenGTIReview.mp4")
                        .build()
        );

        repository.saveAll(movies);
        return ResponseEntity.ok("Added " + movies.size() + " new movies to the library!");
    }
}