import FormationService from '$lib/services/FormationService';

/*
 * API endpoint: /api/formation/video/watched
 * Method: POST
 * Request: videoId
 * Response: 200
 * Description: Send video watched.
 */
export async function POST({ request, cookies }) {
	try {
		const requestJson = await request.json();

		const videoId = requestJson.quizId;

		// Check if the required parameters are provided
		if (!videoId) {
			console.error('Error: quizId is required');
			return new Response(JSON.stringify({ error: 'quizId is required' }), { status: 400 });
		}

		const response = await FormationService.setVideoWatched(cookies, videoId);

		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while setting video watched: Internal server error');
			return new Response(JSON.stringify({ error: 'Internal server error' }), { status: 500 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}
