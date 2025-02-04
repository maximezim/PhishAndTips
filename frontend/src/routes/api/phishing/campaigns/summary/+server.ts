import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/campaigns/summary
 * Method : GET
 * Request : Campaign id
 * Response : Campaign summary
 * Description : Get campaign summary
 */
export async function GET({ url, cookies }) {
	try {
		const groupId = url.searchParams.get('id');
		if (!groupId) {
			console.error('Error: campaignId is required');
			return new Response(JSON.stringify({ error: 'campaignId is required' }), { status: 400 });
		}
		const response = await PhishingService.getCampaignSummary(cookies, Number(groupId));
		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}
