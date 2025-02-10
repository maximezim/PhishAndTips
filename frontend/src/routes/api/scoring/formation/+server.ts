import ScoringService from '$lib/services/ScoringService';

/*
 * API endpoint : /api/scoring/formation
 * Method : GET
 * Response : Score as a double
 * Description : Score of the user for the formation part
 */
export async function GET({ cookies }) {
	try {
		const response = await ScoringService.getFormationScore(cookies);
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
