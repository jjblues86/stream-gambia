package com.streamgambia.video.controller;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.service.VideoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/videos")
public class VideoController {

    private final VideoService videoService;

    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }

    // This is the ONLY endpoint we need right now
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // We only call the method that actually exists
            videoService.uploadVideo(file);
            return "Video upload started! Transcoding in progress...";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error uploading video: " + e.getMessage();
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Video> getAllVideos() {
        return videoService.getAllVideos();
    }
}