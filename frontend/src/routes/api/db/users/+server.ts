import DbService from '$lib/services/DbService';

export async function GET({ url, cookies }) {
	try {
		console.log('Log: enter try');
		const response = await DbService.getUsers(cookies);
		console.log(response);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Erreur: la réponse est vide');
			return new Response(JSON.stringify({ error: "No data found" }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
	}
}