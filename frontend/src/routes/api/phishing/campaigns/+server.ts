import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/campaigns
 * Method : GET
 * Response : list of campaigns
 * Description : Get all campaigns
 */
export async function GET({ cookies }) {
	try {
		const response = await PhishingService.getCampaigns(cookies);
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

/*
 * API endpoint : /api/phishing/campaigns
 * Method : POST
 * Request : a campaign parsed in json
 * Response : the campaign created
 * Description : Create a new campaign
 */
export async function POST({ request, cookies }) {
	const campaignJson = await request.json();
	try {
		const response = await PhishingService.createCampaign(cookies, campaignJson);
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

/*
 * API endpoint : /api/phishing/campaigns
 * Method : PUT
 * Request : a campaign parsed in json
 * Response : the campaign updated
 * Description : Update an existing campaign
 */
export async function PUT({ request, cookies }) {
	const campaignJson = await request.json();
	try {
		const response = await PhishingService.updateCampaign(cookies, campaignJson.id, campaignJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

/*
 * API endpoint : /api/phishing/campaigns
 * Method : DELETE
 * Request : a campaign id
 * Response : a success message
 * Description : Delete an existing campaign
 */
export async function DELETE({ request, cookies }) {
	// Read campaign id from URL query parameters
	const url = new URL(request.url);
	const campaignIdStr = url.searchParams.get('id');
	if (!campaignIdStr) return new Response(JSON.stringify({ error: 'Campaign id is required' }), { status: 400 });
	const campaignId = parseInt(campaignIdStr);
	try {
		const response = await PhishingService.deleteCampaign(cookies, campaignId);
		if (response && response.success) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: Campaign not found');
			return new Response(JSON.stringify({ error: 'Campaign not found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
