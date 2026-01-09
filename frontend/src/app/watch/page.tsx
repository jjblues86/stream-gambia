"use client";

import { useSearchParams } from "next/navigation";
import VideoPlayer from "../../components/VideoPlayer";
import { Suspense } from "react";

function WatchContent() {
  const searchParams = useSearchParams();
  // Get the 'v' parameter from the URL (e.g. /watch?v=http://...)
  const videoUrl = searchParams.get("v");

  return (
    <div className="min-h-screen bg-black text-white flex flex-col items-center justify-center">
      <div className="w-full max-w-4xl p-4">
        <h1 className="text-2xl font-bold mb-4 text-red-500">Now Watching</h1>

        {videoUrl ? (
          <div className="border border-gray-800 rounded overflow-hidden shadow-2xl shadow-red-900/20">
            <VideoPlayer src={videoUrl} />
          </div>
        ) : (
          <div className="text-gray-500 text-center">
            <p>No video selected.</p>
            <p className="text-sm mt-2">Try uploading a video first!</p>
          </div>
        )}
      </div>
    </div>
  );
}

// We wrap it in Suspense because useSearchParams causes client-side hydration needs
export default function WatchPage() {
  return (
    <Suspense fallback={<div className="text-white">Loading...</div>}>
      <WatchContent />
    </Suspense>
  );
}