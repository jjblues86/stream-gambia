package com.streamgambia.video.controller;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.service.FileService;
import com.streamgambia.video.service.VideoService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/videos")
@RequiredArgsConstructor
public class VideoController {

    private final VideoService videoService;
    private final FileService fileService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Video> uploadVideo(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description")String description,
            @RequestParam("director") String director,
            @RequestParam("genre") String genre
    ) {
        return ResponseEntity.ok(videoService.saveVideo(file, title, description, director, genre));
    }

    @GetMapping("/{videoId}/stream")
    public ResponseEntity<Resource> streamVideo(@PathVariable("videoId") String videoId) {
        Video video = videoService.getVideoMetadata(videoId);
        
        String contentType = video.getContentType();
        if (contentType == null || contentType.isEmpty()) {
            contentType = "video/mp4";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(fileService.downloadFile(video.getVideoUrl())));
    }
}
