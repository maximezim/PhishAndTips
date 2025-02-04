import AuthService from '$lib/services/AuthService.js';

/*
 * API endpoint : /api/auth/get-user
 * Method : GET
 * Response : user object
 * Description : Get the user currently connected
 */
export async function GET({ cookies }) {
	try {
		const response = await AuthService.getUser(cookies);
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
