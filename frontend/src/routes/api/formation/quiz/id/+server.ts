import FormationService from '$lib/services/FormationService';

/*
 * API endpoint : /api/formation/quiz/id
 * Method : GET
 * Parameter : Quiz id
 * Response : Quiz
 * Description : Get quiz
 */
export async function GET({ url, cookies }) {
	try {
		const quizId = url.searchParams.get('quizId'); // get email from parameters
		if (!quizId) {
			console.error('Error: quizId is required');
			return new Response(JSON.stringify({ error: 'quizId is required' }), { status: 400 });
		}
		const response = await FormationService.getQuizById(cookies, quizId);
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
