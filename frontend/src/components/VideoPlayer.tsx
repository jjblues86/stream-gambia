"use client";

import { useEffect, useRef } from "react";
import Hls from "hls.js";

interface VideoPlayerProps {
  src: string;
  poster?: string;
}

export default function VideoPlayer({ src, poster }: VideoPlayerProps) {
  const videoRef = useRef<HTMLVideoElement>(null);

  useEffect(() => {
    const video = videoRef.current;
    if (!video) return;

    // Check if the browser supports HLS natively (Safari) or needs hls.js (Chrome/Firefox)
    if (Hls.isSupported()) {
      const hls = new Hls();
      hls.loadSource(src);
      hls.attachMedia(video);
    } else if (video.canPlayType("application/vnd.apple.mpegurl")) {
      video.src = src;
    }
  }, [src]);

  return (
 // inside VideoPlayer.tsx
 <video
     controls
     className="w-full h-full"
     // poster="https://via.placeholder.com/..."  <--- DELETE THIS LINE
 >
     <source src={src} type="application/x-mpegURL" />
 </video>
  );
}