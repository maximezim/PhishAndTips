import type { LayoutServerLoad } from './$types.js';
import { redirect } from '@sveltejs/kit';
import AuthService from '$lib/services/AuthService';

export const load: LayoutServerLoad = async ({ cookies }) => {
	const isLogged = await AuthService.isLoggedFromServer(cookies);
	if (!isLogged) {
		throw redirect(303, '/login');
	}
	const needChangePassword = await AuthService.needChangePasswordFromServer(cookies);
	if (needChangePassword) {
		throw redirect(303, '/change-password');
	}
};
