package com.streamgambia.video.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "watchlist", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"userId", "videoId"}) // Prevent duplicate saves
})
public class Watchlist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String userId;  // From Auth Token
    private String videoId; // From Video Service

    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}