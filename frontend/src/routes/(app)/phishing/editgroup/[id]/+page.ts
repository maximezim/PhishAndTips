import type { PageLoad } from './$types';

export const load: PageLoad = async ({ parent, data }) => {
	await parent();
	let { groupDetails } = data;
	return {
		title: 'Phishing',
		dash_text: 'white',
		dash_bg: 'secondary',
		about_text: 'white',
		about_bg: 'secondary',
		form_text: 'white',
		form_bg: 'secondary',
		osint_text: 'white',
		osint_bg: 'secondary',
		pwd_text: 'white',
		pwd_bg: 'secondary',
		phishing_text: 'primary',
		phishing_bg: 'white',

		groupDetails: groupDetails
	};
};
