/** @type {import('next').NextConfig} */
const nextConfig = {
  async rewrites() {
    return [
      //接口请求
      { source: '/:path*', destination: `http://127.0.0.1:8080/:path*` },
    ]
  },
}

export default nextConfig;
