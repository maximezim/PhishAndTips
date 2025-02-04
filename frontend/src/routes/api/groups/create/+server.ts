import PhishingService from '$lib/services/PhishingService.js';
import { json } from '@sveltejs/kit';

export async function POST({ request, cookies }) {
	const groupJson = await request.json();
	try {
		console.log('Log: enter try');
		console.log(groupJson);
		const response = PhishingService.createGroup(cookies, groupJson);
		console.log(response);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.log('IL Y A UNE ERREUR ICIIII');
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
