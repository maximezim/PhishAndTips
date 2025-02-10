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
			console.error('Error while fetching users.');
			return new Response(JSON.stringify({ error: 'Error while fetching users.' }), {
				status: 500
			});
		}
	} catch (e) {
		console.error(e);
	}
}

/*
 * API endpoint : /api/db/users
 * Method : POST
 * Request : CSV file with a list of users
 * Response : list of users
 * Description : Importn a CSV file with users
 */
export async function POST({ cookies, request }) {
	try {
		const formData = await request.formData();
		const file = formData.get('file');

		if (!file || !(file instanceof Blob)) {
			return new Response(JSON.stringify({ error: 'No valid CSV file provided' }), {
				status: 400
			});
		}

		const newFormData = new FormData();
		newFormData.append('file', file);

		const response = await DbService.importCSV(cookies, newFormData);

		if (response) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while importing users CSV file.');
			return new Response(JSON.stringify({ error: 'Error while importing users' }), {
				status: 500
			});
		}
	} catch (e) {
		console.error('API Error:', e);
		return new Response(JSON.stringify({ error: 'Internal Server Error' }), { status: 500 });
	}
}
