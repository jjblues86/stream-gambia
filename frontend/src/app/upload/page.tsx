"use client"; // This is a client-side interactive component

import { useState, ChangeEvent, FormEvent } from "react";

export default function UploadPage() {
  const [file, setFile] = useState<File | null>(null);
  const [status, setStatus] = useState<"idle" | "uploading" | "success" | "error">("idle");
  const [message, setMessage] = useState("");

  const handleFileChange = (e: ChangeEvent<HTMLInputElement>) => {
    if (e.target.files && e.target.files.length > 0) {
      setFile(e.target.files[0]);
    }
  };

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    if (!file) return;

    setStatus("uploading");
    setMessage("Uploading & Transcoding... (Do not close this tab)");

    const formData = new FormData();
    formData.append("file", file);

    try {
      // Use the environment variable we set in Docker
      const apiUrl = "http://localhost:8082";

// Force it to 8082
const res = await fetch("http://localhost:8082/videos/upload", {
    method: 'POST',
    body: formData,
    // ... rest of your code
});
      if (!res.ok) {
        throw new Error("Upload failed");
      }

      setStatus("success");
      setMessage("Success! Video is ready for streaming.");
    } catch (error) {
      console.error(error);
      setStatus("error");
      setMessage("Something went wrong. Please try again.");
    }
  };

  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center p-4">
      <div className="bg-gray-800 p-8 rounded-lg shadow-xl max-w-md w-full border border-gray-700">
        <h1 className="text-2xl font-bold mb-6 text-center text-red-500">
          Upload to StreamGambia
        </h1>

        <form onSubmit={handleSubmit} className="space-y-6">
          {/* File Input */}
          <div className="border-2 border-dashed border-gray-600 rounded-lg p-8 text-center hover:border-red-500 transition-colors cursor-pointer">
            <input
              type="file"
              accept="video/*"
              onChange={handleFileChange}
              className="block w-full text-sm text-gray-400 file:mr-4 file:py-2 file:px-4 file:rounded-full file:border-0 file:text-sm file:font-semibold file:bg-red-600 file:text-white hover:file:bg-red-700"
            />
          </div>

          {/* Status Message */}
          {status === "uploading" && (
            <div className="flex items-center justify-center space-x-2 text-yellow-400">
              <div className="w-4 h-4 border-2 border-current border-t-transparent rounded-full animate-spin"></div>
              <span>Processing... (This may take a minute)</span>
            </div>
          )}

          {status === "success" && (
            <div className="p-3 bg-green-900/50 text-green-400 rounded text-center">
              {message}
            </div>
          )}

          {status === "error" && (
            <div className="p-3 bg-red-900/50 text-red-400 rounded text-center">
              {message}
            </div>
          )}

          {/* Submit Button */}
          <button
            type="submit"
            disabled={!file || status === "uploading"}
            className={`w-full py-3 px-4 rounded font-bold transition-all ${
              !file || status === "uploading"
                ? "bg-gray-600 cursor-not-allowed"
                : "bg-red-600 hover:bg-red-700 text-white shadow-lg shadow-red-600/30"
            }`}
          >
            {status === "uploading" ? "Transcoding..." : "Upload Video"}
          </button>
        </form>
      </div>
    </div>
  );
}