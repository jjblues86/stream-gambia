package com.streamgambia.video.repository;

import com.streamgambia.video.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WatchlistRepository extends JpaRepository<Watchlist, String> {
    List<Watchlist> findByUserId(String userId);
    Optional<Watchlist> findByUserIdAndVideoId(String userId, String videoId);
    void deleteByUserIdAndVideoId(String userId, String videoId);
}