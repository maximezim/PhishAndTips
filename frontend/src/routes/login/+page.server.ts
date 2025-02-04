import type { PageServerLoad, Actions } from './$types.js';
import { fail, redirect } from '@sveltejs/kit';
import { superValidate } from 'sveltekit-superforms';
import { formSchema } from './schema.js';
import { zod } from 'sveltekit-superforms/adapters';
import AuthService from '$lib/services/AuthService';

export const load: PageServerLoad = async ({ cookies }) => {
	const isLogged = await AuthService.isLoggedFromServer(cookies);
	if (isLogged) {
		throw redirect(303, '/dashboard');
	}
	const form = await superValidate(zod(formSchema));
	return { form };
};

export const actions: Actions = {
	default: async (event) => {
		const { cookies } = event;
		const form = await superValidate(event, zod(formSchema));

		if (!form.valid) {
			return fail(400, { form });
		}

		const token = await AuthService.authenticate(form.data.email, form.data.password);

		if (token) {
			cookies.set('authToken', token, {
				path: '/',
				httpOnly: false,
				secure: false,
				sameSite: 'strict',
				maxAge: 60 * 60 * 24 * 7
			});

			throw redirect(303, '/dashboard');
		}

		form.errors['email'] = ['Identifiants incorrects'];
		return fail(401, { form });
	}
};
