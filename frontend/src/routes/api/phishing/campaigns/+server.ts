import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ cookies }) {
	try {
		console.log('Log: enter try');
		const response = await PhishingService.getCampaigns(cookies);
		console.log("SveltePhish ", response);
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

export async function POST({ request, cookies }) {
	const groupJson = await request.json();
	try {
		console.log('Log: enter try');
		console.log(groupJson);
		const response = await PhishingService.createCampaign(cookies, groupJson);
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
