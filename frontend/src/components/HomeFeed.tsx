// src/components/HomeFeed.tsx
"use client";

import { useEffect, useState } from "react";
// Make sure this import matches your file name in components
import VideoPlayer from "./VideoPlayer";

interface Video {
  id: string;
  title: string;
  videoUrl: string;
  description?: string;
}

export default function HomeFeed() {
  const [videos, setVideos] = useState<Video[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetch("http://localhost:8082/videos")
      .then((res) => res.json())
      .then((data) => {
        setVideos(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Failed to fetch videos:", err);
        setLoading(false);
      });
  }, []);

  if (loading) return <div className="text-center mt-10 text-white">Loading StreamGambia...</div>;

  return (
    <main className="min-h-screen bg-gray-900 text-white p-8">
      <h1 className="text-3xl font-bold mb-8 text-red-600">StreamGambia</h1>

      {videos.length === 0 ? (
        <p>No videos found. Go upload one!</p>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
          {videos.map((video) => (
            <div key={video.id} className="bg-gray-800 p-4 rounded-xl">
              <VideoPlayer src={video.videoUrl} />
              <h2 className="mt-4 text-xl font-semibold">{video.title}</h2>
              <p className="text-gray-400 text-sm">{video.description || "No description"}</p>
            </div>
          ))}
        </div>
      )}
    </main>
  );
}