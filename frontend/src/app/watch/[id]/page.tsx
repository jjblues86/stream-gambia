"use client";

import { useEffect, useState, use } from "react";
import { useRouter } from "next/navigation";
import VideoPlayer from "@/src/components/VideoPlayer"; // Ensure path matches your component

interface Video {
  id: string;
  title: string;
  description: string;
  director: string;
  videoUrl: string;
}

// In Next.js 15+, params is a Promise, so we must unwrap it using `use()` or async/await
export default function WatchPage({ params }: { params: Promise<{ id: string }> }) {
  const { id } = use(params); // Unwraps the ID from the URL
  const [video, setVideo] = useState<Video | null>(null);
  const [loading, setLoading] = useState(true);
  const router = useRouter();

  useEffect(() => {
    // Fetch the single video details
    fetch(`http://localhost:8082/videos/${id}`)
      .then((res) => {
        if (!res.ok) throw new Error("Video not found");
        return res.json();
      })
      .then((data) => {
        setVideo(data);
        setLoading(false);
      })
      .catch((err) => {
        console.error("Error fetching video:", err);
        setLoading(false);
      });
  }, [id]);

  if (loading) return <div className="text-center mt-20 text-white">Loading theater...</div>;
  if (!video) return <div className="text-center mt-20 text-white">Video not found.</div>;

  return (
    <main className="min-h-screen bg-black text-white">
      {/* Navbar Placeholder */}
      <nav className="p-4 flex items-center border-b border-zinc-800">
        <button
          onClick={() => router.back()}
          className="text-zinc-400 hover:text-white mr-4"
        >
          ← Back
        </button>
        <h1 className="text-xl font-bold text-red-600">StreamGambia</h1>
      </nav>

      <div className="max-w-6xl mx-auto p-6">
        {/* THEATER MODE PLAYER */}
        <div className="aspect-video w-full bg-black shadow-2xl rounded-lg overflow-hidden border border-zinc-800">
           <VideoPlayer src={video.videoUrl} />
        </div>

        {/* Video Metadata */}
        <div className="mt-6">
          <h1 className="text-3xl font-bold">{video.title}</h1>

          <div className="flex items-center text-zinc-400 text-sm mt-2 space-x-4">
             <span>Director: {video.director || "Unknown"}</span>
             <span>•</span>
             <span>ID: {video.id}</span>
          </div>

          <p className="mt-4 text-zinc-300 leading-relaxed max-w-2xl">
            {video.description || "No description provided for this video."}
          </p>
        </div>
      </div>
    </main>
  );
}