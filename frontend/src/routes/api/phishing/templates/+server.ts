import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/pages
 * Method : GET
 * Response : list of pages
 * Description : Get all pages
 */
export async function GET({ cookies }) {
	try {
		const response = await PhishingService.getTemplates(cookies);
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
