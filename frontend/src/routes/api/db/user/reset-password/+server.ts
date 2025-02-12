import DbService from '$lib/services/DbService';

export async function POST({ request, cookies }) {
	const userJson = await request.json();
	try {
		const response = await DbService.resetUserPassword(cookies, userJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while creating new user.');
			return new Response(JSON.stringify({ error: 'Server error' }), { status: 500 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}