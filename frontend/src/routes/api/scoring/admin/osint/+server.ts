import ScoringService from '$lib/services/ScoringService';

/*
 * API endpoint : /api/scoring/admin/osint
 * Method : POST
 * Request : User email
 * Response : Score as a double
 * Description : Score of the user of the given email for the osint part
 */
export async function POST({ cookies, request }) {
	try {
		const data = await request.json();
		const response = await ScoringService.getAdminOsintScore(cookies, data.email);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while fetching user osint score (Svelte API to Service).');
			return new Response(
				JSON.stringify({
					error: 'Error while fetching user osint score (Svelte API to Service).'
				}),
				{ status: 500 }
			);
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
