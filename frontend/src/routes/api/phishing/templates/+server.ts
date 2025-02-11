import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/templates
 * Method : GET
 * Response : list of templates
 * Description : Get all templates
 */
export async function GET({ cookies }) {
	try {
		const response = await PhishingService.getTemplates(cookies);
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
 * API endpoint : /api/phishing/templates
 * Method : POST
 * Request : a template parsed in json
 * Response : the template created
 * Description : Create a new template
 */
export async function POST({ request, cookies }) {
	const templateJson = await request.json();
	try {
		const response = await PhishingService.createTemplate(cookies, templateJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}

/*
 * API endpoint : /api/phishing/templates
 * Method : PUT
 * Request : a template parsed in json
 * Response : the template updated
 * Description : Update an existing template
 */
export async function PUT({ request, cookies }) {
	const templateJson = await request.json();
	try {
		const response = await PhishingService.updateTemplate(cookies, templateJson.id, templateJson);
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
 * API endpoint : /api/phishing/templates
 * Method : DELETE
 * Request : a template id
 * Response : a success message
 * Description : Delete an existing template
 */
export async function DELETE({ request, cookies }) {
	const groupId = await request.json();
	try {
		const response = await PhishingService.deleteTemplate(cookies, groupId);
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
