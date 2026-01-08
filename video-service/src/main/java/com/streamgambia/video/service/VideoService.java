package com.streamgambia.video.service;

import com.streamgambia.video.entity.Video;
import com.streamgambia.video.repository.VideoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VideoService {

    private final FileService fileService;
    private final VideoRepository videoRepository;
    private final VideoTranscodingService videoTranscodingService;
    private final MinioService minioService;

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

    public List<Video> getAllVideos() {
        return videoRepository.findAll();
    }

    public void uploadVideo(MultipartFile file) throws IOException, InterruptedException {
        // 1. Save MultipartFile to a local temp file
        File tempSource = File.createTempFile("upload_", ".mp4");
        file.transferTo(tempSource);

        // 2. Create a temp directory for the HLS output
        String videoId = UUID.randomUUID().toString();
        String tempOutputDir = System.getProperty("java.io.tmpdir") + "/" + videoId;

        // 3. Run FFmpeg (This might take time!)
        System.out.println("Starting Transcoding for: " + videoId);
        videoTranscodingService.transcodeToHls(tempSource, tempOutputDir);
        System.out.println("Transcoding complete for " + videoId);

        // 4. Upload ALL generate files to MinIO
        File outputDir = new File(tempOutputDir);
        for (File segmentFile : Objects.requireNonNull(outputDir.listFiles())) {
            //Key structure: videos/{videoId}/index.m3u8
            String objectKey = "videos/" + videoId + "/" + segmentFile.getName();

            // Call your existing MinIO upload method here
            // (You might need to tweak it to accept a File instead of MultipartFile)
            minioService.uploadFile(objectKey, segmentFile);
        }

        // 5. Cleanup temp files
        tempSource.delete();

    }
}
