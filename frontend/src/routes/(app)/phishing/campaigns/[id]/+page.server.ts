import type { PageServerLoad, Actions } from './$types.js';
import PhishingService from '$lib/services/PhishingService.js';

export const load: PageServerLoad = async ({ params, cookies }) => {
	const { id } = params;
	const campaignDetails = await PhishingService.getCampaignDetails(cookies, parseInt(id));
	const campaignSummary = await PhishingService.getCampaignSummary(cookies, parseInt(id));

	return {
		campaignDetails: campaignDetails,
		campaignSummary: campaignSummary
	};
};
