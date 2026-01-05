'use client';

import { useEffect, useState } from 'react';
import { useRouter, useParams } from 'next/navigation'; // <--- Added useParams

export default function WatchPage() { // <--- Removed { params } prop
    const [token, setToken] = useState<string | null>(null);
    const router = useRouter();
    const params = useParams(); // <--- Get ID safely from the hook
    const id = params.id;

    useEffect(() => {
        const storedToken = localStorage.getItem('token');
        if (!storedToken) {
            router.push('/');
            return;
        }
        setToken(storedToken);
    }, [router]);

    if (!token) return <div className="text-white">Loading Auth...</div>;

    const streamUrl = `http://localhost:8080/api/v1/videos/${id}/stream?token=${token}`;

    return (
        <main className="flex min-h-screen flex-col items-center bg-black text-white">
            <div className="w-full p-4">
                <button
                    onClick={() => router.back()}
                    className="text-zinc-400 hover:text-white transition"
                >
                    ← Back to Dashboard
                </button>
            </div>

            <div className="flex-1 flex flex-col justify-center w-full max-w-6xl px-4">
                <h1 className="text-2xl font-bold mb-4 text-red-600">Now Playing</h1>

                <div className="relative aspect-video bg-zinc-900 rounded-lg overflow-hidden border border-zinc-800 shadow-2xl">
                    <video
                        controls
                        autoPlay
                        className="w-full h-full"
                        src={streamUrl}
                    >
                        Your browser does not support the video tag.
                    </video>
                </div>

                <p className="mt-4 text-zinc-500 text-sm">
                    Stream ID: {id} <br/>
                    Server: Spring Boot (Video Service)
                </p>
            </div>
        </main>
    );
}