import FormationService from '$lib/services/FormationService';

/*
 * API endpoint : /api/formation/videos
 * Method : GET
 * Response : list of videos
 * Description : Get all videos from formation db
 */
export async function GET({ cookies }) {
	try {
		const response = await FormationService.getVideos(cookies);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error: No data found');
			return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
		return new Response(
			JSON.stringify({ 
				error: 'Internal server error',
				message: e instanceof Error ? e.message : 'Unknown error'
			}), 
			{ status: 500 }
		);
	}
}
