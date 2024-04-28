import { PUBLIC_BACKEND_URL } from '$env/static/public';

export const BACKEND_BASE_PATH = "/api";

if (!PUBLIC_BACKEND_URL) {
    throw new Error('PUBLIC_BACKEND_URL is required');
}
export const BACKEND_URL: string = PUBLIC_BACKEND_URL;
console.log(`Using BACKEND_URL:${BACKEND_URL}`);
