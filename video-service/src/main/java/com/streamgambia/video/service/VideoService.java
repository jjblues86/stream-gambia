package com.streamgambia.video.service;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final FileService fileService;
    private final VideoRepository videoRepository;

    public Video saveVideo(MultipartFile file, String title, String description, String director, String genre) {
        String videoFileName = fileService.uploadFile(file);

        Video video = Video.builder()
                .title(title)
                .description(description)
                .director(director)
                .genre(genre)
                .videoUrl(videoFileName)
                .contentType(file.getContentType())
                .uploadDate(LocalDateTime.now())
                .build();

        return videoRepository.save(video);
    }

    public Video getVideoMetadata(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Video not found"));
    }
}
