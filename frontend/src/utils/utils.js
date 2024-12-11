import { page } from '$app/stores';
import { derived } from 'svelte/store';

/**
 * @param {any} path
 */
export function isActive(path){
    return derived(page, $page => $page.url.pathname === path); 
}