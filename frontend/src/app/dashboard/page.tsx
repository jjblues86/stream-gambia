"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";

// Define what a "Video" looks like
interface Video {
  id: string;
  title: string;
  description: string;
  director: string;
  videoUrl: string;
}

export default function Dashboard() {
  const [videos, setVideos] = useState<Video[]>([]);
  const router = useRouter();

  // Load all videos when the page opens
  useEffect(() => {
    fetch("http://localhost:8082/videos") // Calls Backend Port 8082
      .then((res) => res.json())
      .then((data) => setVideos(data))
      .catch((err) => console.error("Error fetching videos:", err));
  }, []);

  return (
    <main className="min-h-screen bg-black text-white p-8">
      {/* Header */}
      <div className="flex justify-between items-center mb-8">
        <h1 className="text-3xl font-bold text-red-600">StreamGambia</h1>
        <button
          onClick={() => router.push("/upload")}
          className="bg-red-600 px-4 py-2 rounded font-bold hover:bg-red-700 transition"
        >
          + Upload Video
        </button>
      </div>

      {/* Video Grid */}
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
        {videos.map((video) => (
          <div
            key={video.id}
            onClick={() => router.push(`/watch/${video.id}`)} // <--- THE FIX: Navigates to /watch/[id]
            className="bg-zinc-900 rounded-lg overflow-hidden hover:scale-105 transition transform duration-200 cursor-pointer group"
          >
            {/* Thumbnail Placeholder */}
            <div className="aspect-video bg-zinc-800 flex items-center justify-center relative">
              <span className="text-zinc-600 text-4xl group-hover:text-red-600 transition">
                ▶
              </span>
            </div>

            {/* Video Info */}
            <div className="p-4">
              <h3 className="font-bold text-lg truncate">{video.title}</h3>
              <p className="text-zinc-400 text-sm mt-1">{video.director || "Unknown Director"}</p>
            </div>
          </div>
        ))}
      </div>
    </main>
  );
}