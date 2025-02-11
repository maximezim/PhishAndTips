import ScoringService from '$lib/services/ScoringService';

/*
 * API endpoint : /api/scoring/admin/phishing
 * Method : POST
 * Request : User email
 * Response : JSON with count as number/total_number
 * Description : Detailed score of the user of the given email for the phishing part
 */
export async function GET({ cookies }) {
	try {
		const response = await ScoringService.getPhishingDetails(cookies);
		if (response != null) {
            console.log(response);
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while fetching phishing details (Svelte API to Service).');
			return new Response(
				JSON.stringify({
					error: 'Error while fetching phishing details (Svelte API to Service).'
				}),
				{ status: 500 }
			);
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
