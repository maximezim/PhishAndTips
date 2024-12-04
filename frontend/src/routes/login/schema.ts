import { z } from 'zod';

// Validation schema
export const formSchema = z.object({
	email: z
		.string()
		.min(2, { message: 'Veuillez entrer une adresse mail.' })
		.max(100, { message: 'Adresse mail trop longue.' })
		.email({ message: 'Veuillez entrer une adresse mail valide.' }),
	password: z
		.string()
		.min(1, { message: 'Veuillez entrer un mot de passe.' })
		.max(100, { message: 'Mot de passe trop long.' })
});

export type FormSchema = typeof formSchema;
