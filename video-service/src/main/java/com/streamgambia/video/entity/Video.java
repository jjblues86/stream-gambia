package com.streamgambia.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Builder
@RequiredArgsConstructor
@Table(name = "videos")
@AllArgsConstructor
public class Video {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    private String title;
    @Column(length = 1000)
    private String description;
    private String director;
    @ElementCollection
    private List<String> genre;
    @Enumerated(EnumType.STRING)
    private ContentType type;
    private String videoUrl;
    private String contentType;
    private Integer releaseYear;
    private boolean isPremium;
    private String thumbnailUrl;
    private LocalDateTime uploadDate;
}