import PhishingService from '$lib/services/PhishingService.js';

export async function GET({ cookies }) {
	try {
		console.log('Log: enter try');
		const response = PhishingService.getGroups(cookies);
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
			console.error('Erreur: la réponse est vide');
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

export async function PUT({ request, cookies }) {
	const groupJson = await request.json();
	console.log('\n\n\n=========\n\n\ngroupJson : ', groupJson, '\n\n\n=========\n\n\n');
	try {
		console.log('Log: enter try');
		console.log(groupJson);
		const response = PhishingService.updateGroup(cookies, groupJson.id, groupJson);
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

export async function DELETE({ request, cookies }) {
	const groupId = await request.json();
	try {
		console.log('Log: enter try');
		console.log(groupId);
		const response = PhishingService.deleteGroup(cookies, groupId);
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
