import type { PageServerLoad, Actions } from './$types.js';
import { fail, redirect } from '@sveltejs/kit';
import { superValidate } from 'sveltekit-superforms';
import { formSchema } from './schema.js';
import { zod } from 'sveltekit-superforms/adapters';
import AuthService from '$lib/services/AuthService';

// Load validation schema
export const load: PageServerLoad = async ({ cookies }) => {
	const needChangePassword = await AuthService.needChangePasswordFromServer(cookies);
	if (!needChangePassword) {
		throw redirect(303, '/login');
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

		const currentPassword = form.data.current_password;
		const newPassword = form.data.new_password;

		// Attempt to authenticate with the API
		const response = await AuthService.changePasswordFromServer(
			cookies,
			currentPassword,
			newPassword
		);
		if (response === true) {
			throw redirect(303, '/login');
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
