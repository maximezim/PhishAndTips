import FormationService from '$lib/services/FormationService';

// API endpoint : /api/formation/quiz/createQuiz
// Method : POST
export async function POST({ request, cookies }) {
	try {
		const payload = await request.json();
		const { json, title, description } = payload;
		const result = await FormationService.createQuiz(cookies, json, title, description);
        console.log(result);
        const idMatch = typeof result === 'string' ? result.match(/Quiz created successfully with id (\d+)/) : null;
		if (idMatch && idMatch[1]) {
            const id = idMatch[1];
			return new Response(JSON.stringify({ message: "Quiz created", id }), { status: 200 });
		} else {
			return new Response(JSON.stringify({ error: "Quiz creation failed" }), { status: 500 });
		}
	} catch (e) {
		console.error(e);
		return new Response(JSON.stringify({ error: "Internal server error" }), { status: 500 });
	}
}
