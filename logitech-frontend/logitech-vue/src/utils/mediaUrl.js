const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8090';

export function resolveMediaUrl(path) {
  if (!path) return '';
  if (/^https?:\/\//i.test(path)) return path;

  const base = API_BASE_URL.replace(/\/$/, '');
  const normalizedPath = String(path).startsWith('/') ? path : `/${path}`;
  return `${base}${normalizedPath}`;
}
