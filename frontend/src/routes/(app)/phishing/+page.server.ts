import type { PageServerLoad, Actions } from './$types.js';
import PhishingService from '$lib/services/PhishingService.js';

export const load: PageServerLoad = async ({ cookies }) => {
	const dataCamp = await PhishingService.getCampaigns(cookies);
	const dataTemp = await PhishingService.getTemplates(cookies);
	const dataPage = await PhishingService.getPages(cookies);
	const dataGroup = await PhishingService.getGroups(cookies);

	return {
		campaigns: dataCamp,
		templates: dataTemp,
		pages: dataPage,
		groups: dataGroup
	};
};
