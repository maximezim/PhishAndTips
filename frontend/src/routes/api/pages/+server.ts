import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ cookies }) {
	try {
		console.log('Log: enter try');
		const response = PhishingService.getPages(cookies);
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
