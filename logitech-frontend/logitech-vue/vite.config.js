import {fileURLToPath, URL} from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'


export default defineConfig({
  base: '/logitech/',
  server: {
    proxy: {
      "/members": {
        target: process.env.VITE_DEV_API_PROXY || "http://localhost:8090",
        changeOrigin: true
      },
      "/items": {
        target: process.env.VITE_DEV_API_PROXY || "http://localhost:8090",
        changeOrigin: true
      },
      "/carts": {
        target: process.env.VITE_DEV_API_PROXY || "http://localhost:8090",
        changeOrigin: true
      },
      "/orders": {
        target: process.env.VITE_DEV_API_PROXY || "http://localhost:8090",
        changeOrigin: true
      },
      "/images": {
        target: process.env.VITE_DEV_API_PROXY || "http://localhost:8090",
         changeOrigin: true
      }
    }
  },
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)) 
    }
  }
});
