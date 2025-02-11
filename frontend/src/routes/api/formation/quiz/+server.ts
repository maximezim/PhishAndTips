import FormationService from '$lib/services/FormationService';

/*
 * API endpoint : /api/formation/quizzes
 * Method : GET
 * Response : list of quizzes
 * Description : Get all quizzes from formation db
 */
export async function GET({ cookies }) {
	try {
		const response = await FormationService.getQuizzes(cookies);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
	}
}
