import { defineConfig } from 'vitest/config';
import { sveltekit } from '@sveltejs/kit/vite';

export default defineConfig({
	plugins: [sveltekit()],
	optimizeDeps: {
		exclude: ['svelte-sonner']
	  },
	  ssr: {
		noExternal: ['svelte-sonner']
	  },
	test: {
		include: ['src/**/*.{test,spec}.{js,ts}']
	}
});
