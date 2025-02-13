import ScoringService from '$lib/services/ScoringService';
import logger from '$utils/logger';

/*
 * API endpoint : /api/scoring/formation
 * Method : GET
 * Response : Score as a double
 * Description : Score of the user for the formation part
 */
export async function GET({ cookies }) {
	try {
		const response = await ScoringService.getFormationScore(cookies);
		return response;
	} catch (error) {
		logger.error('Error in /api/scoring/formation:', error);

		return new Response(JSON.stringify({ error: 'Internal Server Error' }), {
			status: 500,
			headers: { 'Content-Type': 'application/json' }
		});
	}
}
