package com.streamgambia.video.repository;

import com.streamgambia.video.entity.Video;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VideoRepository extends MongoRepository<Video, String> {
}
