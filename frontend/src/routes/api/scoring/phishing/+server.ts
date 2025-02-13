import ScoringService from '$lib/services/ScoringService';

/*
 * API endpoint : /api/scoring/phishing
 * Method : GET
 * Response : Score as a double
 * Description : Score of the user for the phishing part
 */
export async function GET({ cookies }) {
	try {
		const response = await ScoringService.getPhishingScore(cookies);
		if (response != null) {
			return response;
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
