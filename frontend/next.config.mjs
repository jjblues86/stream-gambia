/** @type {import('next').NextConfig} */
const nextConfig = {
    // We will add image domains here later for movie posters
    images: {
        remotePatterns: [
            {
                protocol: 'https',
                hostname: '**',
            },
        ],
    },
};

export default nextConfig;