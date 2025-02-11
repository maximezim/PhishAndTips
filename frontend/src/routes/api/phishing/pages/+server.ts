import PhishingService from '$lib/services/PhishingService.js';

/*
 * API endpoint : /api/phishing/pages
 * Method : GET
 * Response : list of pages
 * Description : Get all pages
 */
export async function GET({ cookies }) {
	try {
		const response = await PhishingService.getPages(cookies);
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
 * API endpoint : /api/phishing/pages
 * Method : POST
 * Request : a page parsed in json
 * Response : the page created
 * Description : Create a new page
 */
export async function POST({ request, cookies }) {
	const pageJson = await request.json();
	try {
		const response = await PhishingService.createPage(cookies, pageJson);
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
 * API endpoint : /api/phishing/pages
 * Method : PUT
 * Request : a page parsed in json
 * Response : the page updated
 * Description : Update an existing page
 */
export async function PUT({ request, cookies }) {
	const pageJson = await request.json();
	try {
		const response = await PhishingService.updatePage(cookies, pageJson.id, pageJson);
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
 * API endpoint : /api/phishing/pages
 * Method : DELETE
 * Request : a page id
 * Response : a success message
 * Description : Delete an existing page
 */
export async function DELETE({ request, cookies }) {
	const pageId = await request.json();
	try {
		const response = await PhishingService.deletePage(cookies, pageId);
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
