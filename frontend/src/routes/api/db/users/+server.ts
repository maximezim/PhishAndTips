import DbService from '$lib/services/DbService';

/*
 * API endpoint : /api/db/users
 * Method : GET
 * Response : list of users
 * Description : Get all users from db
 */
export async function GET({ cookies }) {
	try {
		const response = await DbService.getUsers(cookies);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
	}
}
