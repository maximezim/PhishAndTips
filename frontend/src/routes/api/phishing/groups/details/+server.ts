import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ url, cookies }) {
	try {
		const groupId = url.searchParams.get("id"); // Récupère l'ID depuis l'URL
		console.log('Log: enter try, groupId:', groupId);
		if (!groupId) {
			console.error('Erreur: Aucun groupId fourni');
			return new Response(JSON.stringify({ error: "groupId est requis" }), { status: 400 });
		}
		const response = await PhishingService.getGroupDetails(cookies, Number(groupId));
		console.log(response);
		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Erreur: la réponse est vide');
			return new Response(JSON.stringify({ error: "Aucune donnée trouvée" }), { status: 404 });
		}
	} catch (e) {
		console.error('Erreur serveur:', e);
	}
}
