import type { PageServerLoad, Actions } from './$types.js';
import PhishingService from '$lib/services/PhishingService.js';

export const load: PageServerLoad = async ({ params, cookies }) => {
	const { id } = params;
	console.log('ID: ', parseInt(id));
	const dataGroupDetails = await PhishingService.getGroupDetails(cookies, parseInt(id));
	return {
		groupDetails: dataGroupDetails
	};
};

export const actions: Actions = {
	edit_group: async (event) => {
		console.log('Bouton clicked');
		return { success: true };
	}
};
