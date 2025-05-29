export const BACKEND_URI = (() => {
  const uri = process.env.REACT_APP_BACKEND_URI;
  if (!uri) throw new Error('Missing REACT_APP_BACKEND_URI in environment.');
  return uri;
})();
