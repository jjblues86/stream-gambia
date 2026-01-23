package com.streamgambia.video.controller;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.entity.Watchlist;
import com.streamgambia.video.repository.VideoRepository;
import com.streamgambia.video.repository.WatchlistRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/watchlist")
@RequiredArgsConstructor
public class WatchlistController {

    private final WatchlistRepository watchlistRepository;
    private final VideoRepository videoRepository;

    // 1. Get User's Watchlist (Returns actual Video objects)
    @GetMapping("/{userId}")
    public ResponseEntity<List<Video>> getUserWatchlist(@PathVariable("userId") String userId) {
        List<String> videoIds = watchlistRepository.findByUserId(userId).stream()
                .map(Watchlist::getVideoId)
                .collect(Collectors.toList());

        List<Video> videos = videoRepository.findAllById(videoIds);
        return ResponseEntity.ok(videos);
    }

    // 2. Add to Watchlist
    @PostMapping("/add")
    public ResponseEntity<String> addToWatchlist(@RequestBody WatchlistRequest request) {
        if (watchlistRepository.findByUserIdAndVideoId(request.userId, request.videoId).isPresent()) {
            return ResponseEntity.badRequest().body("Already in watchlist");
        }

        Watchlist entry = Watchlist.builder()
                .userId(request.userId)
                .videoId(request.videoId)
                .build();

        watchlistRepository.save(entry);
        return ResponseEntity.ok("Added to watchlist");
    }

    // 3. Remove from Watchlist
    @DeleteMapping("/remove")
    @Transactional
    public ResponseEntity<String> removeFromWatchlist(@RequestBody WatchlistRequest request) {
        watchlistRepository.deleteByUserIdAndVideoId(request.userId, request.videoId);
        return ResponseEntity.ok("Removed from watchlist");
    }

    // DTO for requests
    public static class WatchlistRequest {
        public String userId;
        public String videoId;
    }
}