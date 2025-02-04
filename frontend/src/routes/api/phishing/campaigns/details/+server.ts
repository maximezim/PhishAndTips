import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ url, cookies }) {
	try {
		const groupId = url.searchParams.get("id");
		console.log('Log: enter try');
		if(!groupId) {
			console.error('Erreur: Aucun groupId fourni');
			return new Response(JSON.stringify({ error: "groupId is required" }), { status: 400 });
		}
		const response = await PhishingService.getCampaignDetails(cookies, groupId ? parseInt(groupId) : 0);
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