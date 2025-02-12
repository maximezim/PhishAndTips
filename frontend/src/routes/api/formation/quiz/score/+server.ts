import FormationService from '$lib/services/FormationService';

/*
 * API endpoint: /api/formation/quiz
 * Method: POST
 * Request: quizId, userEmail, score
 * Response: Quiz
 * Description: Send quiz score.
 */
export async function POST({ request, cookies }) {
	try {
		const requestJson = await request.json();

		const quizId = requestJson.quizId;
		const useremail = requestJson.useremail;
		const score = requestJson.score;

		// Check if the required parameters are provided
		if (!quizId) {
			console.error('Error: quizId is required');
			return new Response(JSON.stringify({ error: 'quizId is required' }), { status: 400 });
		}
		if (!useremail) {
			console.error('Error: useremail is required');
			return new Response(JSON.stringify({ error: 'useremail is required' }), { status: 400 });
		}
		if (!score) {
			console.error('Error: score is required');
			return new Response(JSON.stringify({ error: 'score is required' }), { status: 400 });
		}

		console.log('Received parameters:', { quizId, useremail, score });

		const response = await FormationService.setQuizScore(cookies, quizId, useremail, score);

		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while fetching quiz: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}
