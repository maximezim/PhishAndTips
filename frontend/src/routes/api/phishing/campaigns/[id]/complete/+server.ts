import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ params, cookies }) {
	const campaignId = parseInt(params.id);
	try {
		const response = await PhishingService.completeCampaign(cookies, campaignId);
		if (response && response.success) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: Campaign could not be completed');
			return new Response(JSON.stringify({ error: 'Campaign could not be completed' }), { status: 404 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}
