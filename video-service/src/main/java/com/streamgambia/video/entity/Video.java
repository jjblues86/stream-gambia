package com.streamgambia.video.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "videos")
public class Video {

    @Id
    private String id;
    private String title;
    private String description;
    private String director;
    private String genre;
    private String videoUrl;    // Renamed from 'url' to match your code
    private String contentType;
    private LocalDateTime uploadDate;

    // 1. Empty Constructor (Required by JPA)
    public Video() {
    }

    // 2. Full Constructor (For the Service to use)
    public Video(String id, String title, String videoUrl, String contentType, LocalDateTime uploadDate) {
        this.id = id;
        this.title = title;
        this.videoUrl = videoUrl;
        this.contentType = contentType;
        this.uploadDate = uploadDate;
        // Optional fields (description, director) can be set later or passed as null
    }

    // 3. Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public String getVideoUrl() { return videoUrl; }
    public void setVideoUrl(String videoUrl) { this.videoUrl = videoUrl; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public LocalDateTime getUploadDate() { return uploadDate; }
    public void setUploadDate(LocalDateTime uploadDate) { this.uploadDate = uploadDate; }
}