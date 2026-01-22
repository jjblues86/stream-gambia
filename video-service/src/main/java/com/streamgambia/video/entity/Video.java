package com.streamgambia.video.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@Table(name = "videos")
@AllArgsConstructor
public class Video {

    @Id
    private String id;
    private String title;
    private String description;
    private String director;
    private String genre;
    private String videoUrl;
    private String contentType;
    private Integer releaseYear;
    private boolean isPremium;
    private String thumbnailUrl;
    private LocalDateTime uploadDate;
}