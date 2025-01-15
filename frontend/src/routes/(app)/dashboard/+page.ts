import type { PageLoad } from './$types';

export const load: PageLoad = async ({ data }) => {
	// Add other client-side properties
	return {
		title: 'Tableau de bord',
		dash_text: 'primary',
		dash_bg: 'white',
		about_text: 'white',
		about_bg: 'secondary',
		form_text: 'white',
		form_bg: 'secondary',
		osint_text: 'white',
		osint_bg: 'secondary',
		pwd_text: 'white',
		pwd_bg: 'secondary',
		phishing_text: 'white',
		phishing_bg: 'secondary'
	};
};
