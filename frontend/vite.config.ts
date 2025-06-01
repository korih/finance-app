import path from "path";
import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";
import { cloudflare } from "@cloudflare/vite-plugin"

// https://vite.dev/config/
export default defineConfig({
  build: {
    outDir: 'build'
  },
  plugins: [react(), tailwindcss(), cloudflare()],
  base: '/',
  resolve: {
    alias: {
      "@": path.resolve(__dirname, "./src/"),
    }
  }
});
