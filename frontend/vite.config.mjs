import { defineConfig, loadEnv } from 'vite';
import { sveltekit } from '@sveltejs/kit/vite';

/** @type {import('vite').UserConfig} */
export default ({mode}) => {
    process.env = Object.assign(process.env, loadEnv(mode, process.cwd(), ''));
    return defineConfig({
        plugins: [sveltekit()],
        server: {
            proxy: {
                "/api": process.env.BACKEND_URL,
            }
        },
    })
};

