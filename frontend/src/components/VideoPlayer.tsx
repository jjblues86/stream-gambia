"use client";

import { useEffect, useRef } from "react";
import videojs from "video.js";
import "video.js/dist/video-js.css";

interface VideoPlayerProps {
  src: string; // The URL of the .m3u8 file
}

export default function VideoPlayer({ src }: VideoPlayerProps) {
  const videoRef = useRef<HTMLDivElement>(null);
  const playerRef = useRef<any>(null);

  useEffect(() => {
    // Make sure Video.js player is only initialized once
    if (!playerRef.current) {
      const videoElement = document.createElement("video-js");
      videoElement.classList.add("vjs-big-play-centered");

      // Append the video element to our div
      if (videoRef.current) {
        videoRef.current.appendChild(videoElement);
      }

      // Initialize the player
      const player = (playerRef.current = videojs(videoElement, {
        controls: true,
        autoplay: false,
        preload: "auto",
        fluid: true, // Makes it responsive
        sources: [
          {
            src: src,
            type: "application/x-mpegURL", // This tells it it's HLS
          },
        ],
      }));
    } else {
      // If player already exists, just update the source
      const player = playerRef.current;
      player.src({ src: src, type: "application/x-mpegURL" });
    }
  }, [src]);

  // Dispose the player when component unmounts (cleanup)
  useEffect(() => {
    const player = playerRef.current;

    return () => {
      if (player && !player.isDisposed()) {
        player.dispose();
        playerRef.current = null;
      }
    };
  }, []);

  return (
    <div data-vjs-player>
      <div ref={videoRef} />
    </div>
  );
}