import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/groups
 * Method : GET
 * Response : list of groups
 * Description : Get all groups
 */
export async function GET({ cookies }) {
	try {
		const response = await PhishingService.getGroups(cookies);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

/*
 * API endpoint : /api/phishing/groups
 * Method : POST
 * Request : a group parsed in json
 * Response : the group created
 * Description : Create a new group
 */
export async function POST({ request, cookies }) {
	const groupJson = await request.json();
	try {
		const response = await PhishingService.createGroup(cookies, groupJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

/*
 * API endpoint : /api/phishing/groups
 * Method : PUT
 * Request : a group parsed in json
 * Response : the group updated
 * Description : Update an existing group
 */
export async function PUT({ request, cookies }) {
	const groupJson = await request.json();
	try {
		const response = await PhishingService.updateGroup(cookies, groupJson.id, groupJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

/*
 * API endpoint : /api/phishing/groups
 * Method : DELETE
 * Request : a group id
 * Response : a success message
 * Description : Delete an existing group
 */
export async function DELETE({ request, cookies }) {
	const groupId = await request.json();
	try {
		const response = await PhishingService.deleteGroup(cookies, groupId);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
