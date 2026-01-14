"use client";

import { useState } from "react";
import { useRouter } from "next/navigation";

export default function UploadPage() {
  const router = useRouter();
  const [uploading, setUploading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();
    setUploading(true);
    setError("");

    const form = e.currentTarget;
    const formData = new FormData();

    // Get the file
    const fileInput = form.elements.namedItem("videoFile") as HTMLInputElement;
    if (!fileInput.files || fileInput.files.length === 0) {
      setError("Please select a video file.");
      setUploading(false);
      return;
    }

    // Add all 4 required fields
    formData.append("file", fileInput.files[0]);
    formData.append("title", (form.elements.namedItem("title") as HTMLInputElement).value);
    formData.append("description", (form.elements.namedItem("description") as HTMLInputElement).value);
    formData.append("director", (form.elements.namedItem("director") as HTMLInputElement).value);

    try {
      const res = await fetch("http://localhost:8082/videos", {
        method: "POST",
        body: formData,
      });

      if (!res.ok) {
        const errText = await res.text();
        throw new Error(errText || "Upload failed");
      }

      // Success! Go to dashboard
      router.push("/dashboard");
    } catch (err: any) {
      console.error(err);
      setError("Upload failed: " + err.message);
    } finally {
      setUploading(false);
    }
  };

  return (
    <main className="min-h-screen bg-black text-white flex items-center justify-center p-6">
      <div className="w-full max-w-lg bg-zinc-900 p-8 rounded-lg shadow-xl border border-zinc-800">
        <h1 className="text-3xl font-bold text-red-600 mb-6 text-center">Upload to StreamGambia</h1>

        {error && (
          <div className="bg-red-900/50 text-red-200 p-3 rounded mb-4 text-sm border border-red-800">
            {error}
          </div>
        )}

        <form onSubmit={handleSubmit} className="space-y-5">
          {/* Title Input */}
          <div>
            <label className="block text-zinc-400 text-sm mb-1">Movie Title</label>
            <input name="title" required type="text" placeholder="Enter title..." className="w-full bg-black border border-zinc-700 rounded p-3 text-white focus:border-red-600 outline-none" />
          </div>

          {/* Director Input */}
          <div>
            <label className="block text-zinc-400 text-sm mb-1">Director</label>
            <input name="director" required type="text" placeholder="Director's name..." className="w-full bg-black border border-zinc-700 rounded p-3 text-white focus:border-red-600 outline-none" />
          </div>

          {/* Description Input */}
          <div>
            <label className="block text-zinc-400 text-sm mb-1">Description</label>
            <textarea name="description" required rows={3} placeholder="What is this video about?" className="w-full bg-black border border-zinc-700 rounded p-3 text-white focus:border-red-600 outline-none" />
          </div>

          {/* File Picker */}
          <div className="border-2 border-dashed border-zinc-700 rounded-lg p-6 text-center hover:border-red-600 transition cursor-pointer">
            <input
              name="videoFile"
              type="file"
              accept="video/*"
              required
              className="w-full text-sm text-zinc-400 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-red-600 file:text-white hover:file:bg-red-700 cursor-pointer"
            />
          </div>

          {/* Submit Button */}
          <button
            type="submit"
            disabled={uploading}
            className={`w-full py-3 rounded font-bold text-white transition ${
              uploading ? "bg-zinc-700 cursor-not-allowed" : "bg-red-600 hover:bg-red-700"
            }`}
          >
            {uploading ? "Uploading..." : "Upload Video"}
          </button>
        </form>
      </div>
    </main>
  );
}