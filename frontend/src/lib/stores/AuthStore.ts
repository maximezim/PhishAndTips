import { writable } from 'svelte/store';

export const isAuthenticated = writable(false);
export const token = writable<string | null>(null);

export const setAuthStatus = (authStatus: boolean, authToken: string | null) => {
  isAuthenticated.set(authStatus);
  token.set(authToken);
};
