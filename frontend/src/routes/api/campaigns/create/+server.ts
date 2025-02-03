import { json, Server } from '@sveltejs/kit';
import PhishingService from '$lib/services/PhishingService.js';
import { sortUserPlugins } from 'vite';

export async function POST({ request, cookies }) {
	const { groupJson } = await request.json();
	try {
		console.log('Log: enter try');
		console.log(groupJson);
		const response = PhishingService.createCampaign(cookies, groupJson);
		console.log(response);
		if (response != null) {
			return response;
		} else {
			console.log('IL Y A UNE ERREUR ICIIII');
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
