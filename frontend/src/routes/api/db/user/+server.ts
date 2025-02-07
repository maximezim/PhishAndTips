import DbService from '$lib/services/DbService';

/*
 * API endpoint : /api/db/user
 * Method : GET
 * Parameter : User id
 * Response : User details
 * Description : Get user details
 */
export async function GET({ url, cookies }) {
	try {
		const userEmail = url.searchParams.get('email'); // get email from parameters
		if (!userEmail) {
			console.error('Error: userEmail is required');
			return new Response(JSON.stringify({ error: 'userEmail is required' }), { status: 400 });
		}
		const response = await DbService.getUser(cookies, userEmail);
		if (response) {
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
