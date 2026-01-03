'use client'; // Required for Buttons/Inputs in Next.js

import { useState } from 'react';
import api from "@/src/utils/api";
// import api from '@/utils/api';
import { useRouter } from 'next/navigation';

export default function LoginPage() {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState('');
    const router = useRouter();

    const handleLogin = async (e: React.FormEvent) => {
        e.preventDefault();
        try {
            // 1. Call the Auth Service (via Gateway)
            const response = await api.post('/auth/login', { email, password });

            // 2. Save the Token
            localStorage.setItem('token', response.data.token);

            // 3. Redirect to the Dashboard (We will build this next)
            alert('Login Successful! Token saved.');
            router.push('/dashboard');
        } catch (err: any) {
            setError('Login Failed. Check your credentials.');
            console.error(err);
        }
    };

    return (
        <main className="flex min-h-screen flex-col items-center justify-center bg-black text-white">
            <div className="w-full max-w-md p-8 bg-zinc-900 rounded-lg shadow-lg border border-zinc-800">
                <h1 className="text-3xl font-bold text-red-600 mb-6 text-center">StreamGambia</h1>

                <form onSubmit={handleLogin} className="flex flex-col gap-4">

                    <input
                        type="email"
                        placeholder="Email"
                        className="p-3 rounded bg-zinc-800 text-white border border-zinc-700 focus:border-red-600 outline-none"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                    />

                    <input
                        type="password"
                        placeholder="Password"
                        className="p-3 rounded bg-zinc-800 text-white border border-zinc-700 focus:border-red-600 outline-none"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                    />

                    {error && <p className="text-red-500 text-sm">{error}</p>}

                    <button
                        type="submit"
                        className="bg-red-600 hover:bg-red-700 text-white font-bold py-3 rounded transition"
                    >
                        Sign In
                    </button>
                </form>

                <p className="mt-4 text-zinc-500 text-center text-sm">
                    Don't have an account? Use Postman to Register first!
                </p>
            </div>
        </main>
    );
}