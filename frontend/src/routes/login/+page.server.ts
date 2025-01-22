import type { PageServerLoad, Actions } from './$types.js';
import { fail, redirect } from '@sveltejs/kit';
import { superValidate } from 'sveltekit-superforms';
import { formSchema } from './schema.js';
import { zod } from 'sveltekit-superforms/adapters';
import AuthService from '$lib/services/AuthService';

// Load validation schema
export const load: PageServerLoad = async ({ cookies }) => {
	const isLogged = await AuthService.isLoggedFromServer(cookies);
	if (isLogged) {
		throw redirect(303, '/dashboard');
	}
	return {
		form: await superValidate(zod(formSchema))
	};
};

export const actions: Actions = {
	default: async (event) => {
		const { cookies } = event;
		// Validate form data
		const form = await superValidate(event, zod(formSchema));
		if (!form.valid) {
			// Form validation failed
			return fail(400, {
				form
			});
		}

		const email = form.data.email;
		const password = form.data.password;

		// Attempt to authenticate with the API
		const response = await AuthService.authenticate(email, password);
		if (response != 'error') {
			// Authentication
			const token = response;
			cookies.set('authToken', token, {
				path: '/',
				httpOnly: false,
				secure: false,
				sameSite: 'strict',
				maxAge: 60 * 60 * 24 * 7
			});

			throw redirect(303, '/dashboard');
		} else {
			// Authentication failed
			return fail(401, {
				form: {
					errors: {
						email: 'Identifiants incorrects'
					}
				}
			});
		}
	}
};
