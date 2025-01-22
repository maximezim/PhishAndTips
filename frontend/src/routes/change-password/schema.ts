import { z } from 'zod';

// Validation schema
export const formSchema = z
	.object({
		current_password: z
			.string()
			.min(1, { message: 'Veuillez entrer un mot de passe.' })
			.max(100, { message: 'Mot de passe trop long.' }),
		new_password: z
			.string()
			.min(1, { message: 'Veuillez entrer un mot de passe.' })
			.max(100, { message: 'Mot de passe trop long.' })
			.regex(/^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{10,}$/, {
				message:
					'Le mot de passe doit contenir au moins 10 caractères, une lettre majuscule, une lettre minuscule, un chiffre et un caractère spécial.'
			}),
		confirm_password: z
			.string()
			.min(1, { message: 'Veuillez entrer un mot de passe.' })
			.max(100, { message: 'Mot de passe trop long.' })
	})
	.refine((data) => data.new_password === data.confirm_password, {
		message: 'Les mots de passe ne correspondent pas.',
		path: ['confirm_password']
	});

export type FormSchema = typeof formSchema;
