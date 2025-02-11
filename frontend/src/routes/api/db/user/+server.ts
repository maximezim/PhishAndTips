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
			console.error('Error while fetching user data: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 500 });
		}
	} catch (e) {
		console.error('Server error:', e);
		throw e;
	}
}

/*
 * API endpoint : /api/db/user
 * Method : POST
 * Request : a user parsed in json
 * Response : the user created
 * Description : Create a new user
 */
export async function POST({ request, cookies }) {
	const userJson = await request.json();
	try {
		const response = await DbService.createUser(cookies, userJson);
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

/*
 * API endpoint : /api/db/user
 * Method : PUT
 * Request : a user parsed in json
 * Response : the user updated
 * Description : Update an existing user
 */
export async function PUT({ request, cookies }) {
	const userJson = await request.json();
	try {
		const response = await DbService.updateUser(cookies, userJson);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while updating user.');
			return new Response(JSON.stringify({ error: 'Server error' }), { status: 500 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}

/*
 * API endpoint : /api/db/user
 * Method : DELETE
 * Request : a user email
 * Response : a success message
 * Description : Delete an existing user
 */
export async function DELETE({ request, cookies }) {
	const userEmail = await request.json();
	try {
		const response = await DbService.deleteUser(cookies, userEmail);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while deleting user.');
			return new Response(JSON.stringify({ error: 'Server error' }), { status: 500 });
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
