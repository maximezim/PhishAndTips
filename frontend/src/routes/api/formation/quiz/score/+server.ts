import FormationService from '$lib/services/FormationService';

/*
 * API endpoint: /api/formation/quiz/score
 * Method: POST
 * Request: quizId, score
 * Response: Quiz
 * Description: Send quiz score.
 */
export async function POST({ request, cookies }) {
	try {
		const requestJson = await request.json();

		const quizId = requestJson.quizId;
		const score = requestJson.score;

		// Check if the required parameters are provided
		if (!quizId) {
			console.error('Error: quizId is required');
			return new Response(JSON.stringify({ error: 'quizId is required' }), { status: 400 });
		}
		if (!score) {
			console.error('Error: score is required');
			return new Response(JSON.stringify({ error: 'score is required' }), { status: 400 });
		}

		const response = await FormationService.setQuizScore(cookies, quizId, score);

		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error setting quiz score: Internal server error');
			return new Response(JSON.stringify({ error: 'Internal server error' }), { status: 500 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}
