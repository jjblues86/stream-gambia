package com.streamgambia.video.service;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final MinioService minioService; // Make sure this matches your file name (FileService vs MinioService)
    private final VideoTranscodingService videoTranscodingService;
    private final VideoRepository videoRepository; // <--- NEW: Database connection


    // ✅ CHANGE SIGNATURE: Accept Title, Description, and Director
    public Video uploadVideo(MultipartFile file, String title, String description, String director) throws IOException, InterruptedException {

        // 1. Setup paths (Keep existing logic)
        Path tempSourcePath = Files.createTempFile("upload_", ".mp4");
        File tempSource = tempSourcePath.toFile();
        file.transferTo(tempSource);

        String videoId = UUID.randomUUID().toString();
        String tempOutputDir = System.getProperty("java.io.tmpdir") + "/" + videoId;
        new File(tempOutputDir).mkdirs();

        // 2. Transcode (Keep existing logic)
        System.out.println("Starting Transcoding for: " + videoId);
        videoTranscodingService.transcodeToHls(tempSource, tempOutputDir);

        // 3. Upload to MinIO (Keep existing logic)
        File outputDir = new File(tempOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            for (File segmentFile : Objects.requireNonNull(outputDir.listFiles())) {
                // Note: Added "videos/" prefix to keep bucket organized
                String objectKey = videoId + "/" + segmentFile.getName();
                minioService.uploadFile(objectKey, segmentFile);
            }
        }

        // 4. SAVE TO DB 💾 (Updated to use Real Data)
        String videoUrl = "http://localhost:9000/videos/" + videoId + "/index.m3u8";

        Video video = new Video();
        video.setId(videoId);
        video.setTitle(title);             // <--- Use the Real Title
        video.setDescription(description); // <--- Use the Real Description
        video.setDirector(director);       // <--- Use the Real Director
        video.setVideoUrl(videoUrl);
        video.setContentType(file.getContentType());
        // video.setCreatedAt(LocalDateTime.now()); // Uncomment if you have this field

        videoRepository.save(video);
        System.out.println("Saved Video Metadata: " + video.getTitle());

        // 5. Cleanup
        tempSource.delete();
        // (Optional) recursively delete tempOutputDir here too
        return video;
    }

    // NEW: Method to get list of videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public Video getVideoById(String id) {
        return videoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Video not found with id: " + id));
    }
}