'use client'

import {useEffect, useState} from "react";
import {useRouter} from "next/navigation";
import api from "@/src/utils/api";
import {error} from "next/dist/build/output/log";

interface Video {
    id: string;
    title: string;
    description: string;
    director: string;
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
            // This automatically attached the JWT from localStorage
            const response = await api.get('/videos');
            setVideos(response.data);
        } catch (error) {
        console.error("failed to fecth videos", error);
        // If unauthorize, kick them back to login
        router.push('/login');
    } finally {
        setLoading(false);
    }
};

    const handleLogout = () => {
    localStorage.removeItem('token');
    router.push('/login');
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
                <p>Loading your stream...</p>
            ) : (
                <div className="grid grid-cols-1 md:grid-cols-3 lg:grid-cols-4 gap-8">
                    {videos.length === 0 ? (
                        <div className="col-span-full text-center text-zinc-500">
                            <p className="text-xl">No movies found.</p>
                            <p className="text-sm">Upload one via Postman to see it here!</p>
                        </div>
                    ) : (
                        videos.map((video) => (
                            <div key={video.id} onClick={() => router.push(`/watch/${video.id}`)} className="bg-zinc-900 rounded-lg overflow-hidden hover:scale-105 transition transform duration-200 cursor-pointer">
                                {/* Placeholder for Thumbnail */}
                                <div className="h-40 bg-zinc-800 flex items-center justify-center">
                                    <span className="text-4xl">🎬</span>
                                </div>

                                <div className="p-4">
                                    <h3 className="text-lg font-bold truncate">{video.title}</h3>
                                    <p className="text-zinc-400 text-sm mt-1">{video.director}</p>
                                </div>
                            </div>
                        ))
                    )}
                </div>
            )}
        </main>
    );
}