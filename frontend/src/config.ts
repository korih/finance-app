import axios from "axios";

export const BACKEND_URI = (() => {
  const uri = import.meta.env.VITE_BACKEND_URI;
  if (!uri) throw new Error("Missing VITE_BACKEND_URI in environment.");
  return uri;
})();

export const axiosClient = axios.create({
  baseURL: BACKEND_URI,
  withCredentials: true,
});

// TODO look at the interceptors
axiosClient.interceptors.response.use(
  response => response,
  error => {
    if (error.response?.status === 401) {
      // Optional: redirect to login or clear auth
    }
    return Promise.reject(error);
  }
);
