package com.streamgambia.video.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class VideoTranscodingService {

    // 1. Takes the raw video file and creates an HLS playlist (.m3u8) + segments (.ts)
    public File transcodeToHls(File sourceFile, String outputDir) throws IOException, InterruptedException {
        // Ensure output directory exists
        File outdir = new File(outputDir);
        if (!outdir.exists()) {
            outdir.mkdirs();
        }

        // The name of the master playlist
        String m3u8File = outputDir + "/index.m3u8";

        // 2. Build the FFmpeg command
        // -i: Input file
        // -c:v libx264: Video codec (H.264 is standard)
        // -c:a aac: Audio codec
        // -hls_time 10: Cut into 10-second chunks
        // -hls_list_size 0: Include all chunks in the list
        // -f hls: Format is HLS
        ProcessBuilder builder = new ProcessBuilder(
                "ffmpeg",
                "-i", sourceFile.getAbsolutePath(),
                "-c:v", "libx264",
                "-c:a", "aac",
                "-hls_time", "10",
                "-hls_list_size", "0",
                "-f", "hls",
                m3u8File
        );

        // Redirect logs to console (so you can see FFmpeg working in Docker logs)
        builder.redirectErrorStream(true);
        builder.redirectOutput(ProcessBuilder.Redirect.INHERIT);

        // 3. Run the command
        Process process = builder.start();
        int exitCode = process.waitFor();

        if (exitCode != 0) {
            throw new RuntimeException("FFmpeg transcoding failed with exit code " + exitCode);
        }

        return outdir;
    }
}
