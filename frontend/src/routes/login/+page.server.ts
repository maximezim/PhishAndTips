import type { PageServerLoad, Actions } from './$types.js';
import { fail, redirect } from '@sveltejs/kit';
import { superValidate } from 'sveltekit-superforms';
import { formSchema } from './schema.js';
import { zod } from 'sveltekit-superforms/adapters';

// Load validation schema
export const load: PageServerLoad = async () => {
	return {
		form: await superValidate(zod(formSchema))
	};
};

export const actions: Actions = {
	default: async (event) => {
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
		const response = await fetch(import.meta.env.VITE_API_URL + '/authenticate', {
			method: 'POST',
			headers: {
				'Content-Type': 'application/json'
			},
			body: JSON.stringify({ email, password })
		});

		if (response.ok) {
			// Successful authentication
			const body = await response.json();
			const token = body.jwtToken;
			return redirect(303, '/dashboard');
		} else {
			// Authentication failed
			return fail(400, {
				form: {
					...form,
					errors: {
						email: 'Mot de passe ou email invalide.',
						password: 'Mot de passe ou email invalide.'
					}
				}
			});
		}
	}
};
