import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ request, cookies }) {
	try {
		const groupId = await request.json();
		console.log('Log: enter try');
		const response = PhishingService.getGroupDetails(cookies, groupId);
		console.log(response);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Erreur: la réponse est vide');
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
