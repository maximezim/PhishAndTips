import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/groups/details
 * Method : GET
 * Request : Group id
 * Response : Group details
 * Description : Get group details
 */
export async function GET({ url, cookies }) {
	try {
		const groupId = url.searchParams.get('id'); // get id from parameters
		if (!groupId) {
			console.error('Error: groupId is required');
			return new Response(JSON.stringify({ error: 'groupId is required' }), { status: 400 });
		}
		const response = await PhishingService.getGroupDetails(cookies, Number(groupId));
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
