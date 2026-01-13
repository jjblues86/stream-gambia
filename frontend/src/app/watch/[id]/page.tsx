"use client";

import { useEffect, useState } from "react";
import { useParams, useRouter } from "next/navigation";
import VideoPlayer from "@/src/components/VideoPlayer";

interface Video {
  id: string;
  title: string;
  description: string;
  director: string;
  videoUrl: string;
}

export default function WatchPage() {
  const params = useParams(); // Get the whole object
  const id = params?.id;      // Safely extract ID
  const router = useRouter();
  const [video, setVideo] = useState<Video | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // DEBUGGING LOGS
    console.log("Current Params:", params);
    console.log("Video ID to fetch:", id);

    if (!id) {
        console.warn("No ID found! Waiting...");
        return;
    }

    console.log("Fetching URL:", `http://localhost:8082/videos/${id}`);

    fetch(`http://localhost:8082/videos/${id}`)
      .then((res) => {
        console.log("Response Status:", res.status);
        if (!res.ok) throw new Error("Video not found");
        return res.json();
      })
      .then((data) => {
        console.log("Video Data Received:", data);
        setVideo(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Fetch Error:", err);
        setLoading(false);
      });
  }, [id, params]);

  if (loading) return <div className="text-white text-center mt-20">Loading Theater... (Check Console)</div>;
  if (!video) return <div className="text-white text-center mt-20">Video not found.</div>;

  return (
    <main className="min-h-screen bg-black text-white">
      <nav className="p-4 flex items-center border-b border-zinc-800 bg-zinc-900">
        <button onClick={() => router.back()} className="text-zinc-400 hover:text-white mr-4">← Back</button>
        <h1 className="text-xl font-bold text-red-600">StreamGambia</h1>
      </nav>
      <div className="max-w-6xl mx-auto p-6">
        <div className="aspect-video w-full bg-black shadow-2xl rounded-lg overflow-hidden border border-zinc-800">
           <VideoPlayer src={video.videoUrl} />
        </div>
        <div className="mt-6">
          <h1 className="text-3xl font-bold">{video.title}</h1>
          <p className="mt-4 text-zinc-300">{video.description}</p>
        </div>
      </div>
    </main>
  );
}