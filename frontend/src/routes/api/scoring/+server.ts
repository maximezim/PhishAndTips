import ScoringService from '$lib/services/ScoringService';

/*
 * API endpoint : /api/scoring
 * Method : GET
 * Response : Total score as a double
 * Description : Total score of the user
 */
export async function GET({ cookies }) {
	try {
		const response = await ScoringService.getTotalScore(cookies);
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
