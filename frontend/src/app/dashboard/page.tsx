'use client'

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
// Make sure this path matches your file name!
import VideoPlayer from "../../components/VideoPlayer";

interface Video {
    id: string;
    title: string;
    description: string;
    director: string;
    videoUrl: string; // <--- ADDED THIS (Essential!)
}

export default function DashboardPage() {
    const [videos, setVideos] = useState<Video[]>([]);
    const [loading, setLoading] = useState(true);
    const router = useRouter();

    useEffect(() => {
        fetchVideos();
    }, []);

    const fetchVideos = async () => {
        try {
            // FIXED: Bypass the Gateway (8080) and go straight to Service (8082)
            // This avoids the CORS error and gets your data immediately.
            const response = await fetch('http://localhost:8082/videos');

            if (!response.ok) {
                throw new Error('Network response was not ok');
            }

            const data = await response.json();
            setVideos(data);
        } catch (error) {
            console.error("failed to fetch videos", error);
        } finally {
            setLoading(false);
        }
    };

    const handleLogout = () => {
        localStorage.removeItem('token');
        router.push('/');
    };

    return (
        <main className="min-h-screen bg-black text-white p-8">
            {/* Header */}
            <div className="flex justify-between items-center mb-12">
                <h1 className="text-4xl font-bold text-red-600">StreamGambia</h1>
                <button
                    onClick={handleLogout}
                    className="bg-zinc-800 hover:bg-zinc-700 text-white px-4 py-2 rounded"
                >
                    Logout
                </button>
            </div>

            {/* Movie Grid */}
            {loading ? (
                <p className="text-center text-zinc-500 mt-20">Loading your stream...</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-8">
                    {videos.length === 0 ? (
                        <div className="col-span-full text-center text-zinc-500">
                            <p className="text-xl">No movies found.</p>
                            <p className="text-sm">Go to /upload to add one!</p>
                        </div>
                    ) : (
                        videos.map((video) => (
                            <div key={video.id} className="bg-zinc-900 rounded-lg overflow-hidden hover:scale-105 transition transform duration-200">

                                {/* ACTUAL VIDEO PLAYER (Replaces the 🎬) */}
                                <div className="aspect-video w-full bg-black">
                                    <VideoPlayer src={video.videoUrl} />
                                </div>

                                <div className="p-4">
                                    <h3 className="text-lg font-bold truncate">{video.title}</h3>
                                    <p className="text-zinc-400 text-sm mt-1">{video.description || "No description"}</p>
                                    {video.director && (
                                        <p className="text-zinc-500 text-xs mt-2">Dir: {video.director}</p>
                                    )}
                                </div>
                            </div>
                        ))
                    )}
                </div>
            )}
        </main>
    );
}