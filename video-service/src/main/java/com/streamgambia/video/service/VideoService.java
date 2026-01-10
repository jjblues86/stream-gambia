package com.streamgambia.video.service;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
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
public class VideoService {

    private final MinioService minioService; // Make sure this matches your file name (FileService vs MinioService)
    private final VideoTranscodingService videoTranscodingService;
    private final VideoRepository videoRepository; // <--- NEW: Database connection

    // Constructor Injection
    public VideoService(MinioService minioService,
                        VideoTranscodingService videoTranscodingService,
                        VideoRepository videoRepository) {
        this.minioService = minioService;
        this.videoTranscodingService = videoTranscodingService;
        this.videoRepository = videoRepository;
    }

    public void uploadVideo(MultipartFile file) throws IOException, InterruptedException {
        // 1. Setup paths
        Path tempSourcePath = Files.createTempFile("upload_", ".mp4");
        File tempSource = tempSourcePath.toFile();
        file.transferTo(tempSource);

        String videoId = UUID.randomUUID().toString();
        String tempOutputDir = System.getProperty("java.io.tmpdir") + "/" + videoId;
        new File(tempOutputDir).mkdirs();

        // 2. Transcode
        System.out.println("Starting Transcoding for: " + videoId);
        videoTranscodingService.transcodeToHls(tempSource, tempOutputDir);

        // 3. Upload to MinIO
        File outputDir = new File(tempOutputDir);
        if (outputDir.exists() && outputDir.isDirectory()) {
            for (File segmentFile : Objects.requireNonNull(outputDir.listFiles())) {
                String objectKey = "videos/" + videoId + "/" + segmentFile.getName();
                minioService.uploadFile(objectKey, segmentFile);
            }
        }

        // 4. SAVE TO DB (The Missing Piece) 💾
        // Construct the HLS URL (Assuming MinIO is on port 9000)
        String videoUrl = "http://localhost:9000/videos/videos/" + videoId + "/index.m3u8";

        // Create the object using the Constructor (matching the fields we have)
        Video video = new Video(
                videoId,
                file.getOriginalFilename(),
                videoUrl,
                file.getContentType(),
                java.time.LocalDateTime.now() // Set the current time
        );

        // You can set default values for the empty fields if you want:
        video.setDescription("Uploaded via StreamGambia");
        video.setGenre("Uncategorized");

        videoRepository.save(video);
        System.out.println("Saved Video Metadata: " + video.getTitle());

        // 5. Cleanup
        tempSource.delete();
    }

    // NEW: Method to get list of videos
    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }
}