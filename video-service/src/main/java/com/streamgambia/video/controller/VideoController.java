package com.streamgambia.video.controller;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/videos")
//@CrossOrigin(origins = "*")
@CrossOrigin(origins = "http://localhost:3001")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

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
}