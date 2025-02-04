import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/campaigns/details
 * Method : GET
 * Request : Campaign id
 * Response : Campaign details
 * Description : Get campaign details
 */
export async function GET({ url, cookies }) {
	try {
		const campaignId = url.searchParams.get('id');
		if (!campaignId) {
			console.error('Error: campaignId is required');
			return new Response(JSON.stringify({ error: 'campaignId is required' }), { status: 400 });
		}
		const response = await PhishingService.getCampaignDetails(
			cookies,
			campaignId ? parseInt(campaignId) : 0
		);
		if (response != null) {
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
