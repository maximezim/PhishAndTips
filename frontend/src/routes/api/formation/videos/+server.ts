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
	}
}

/*
 * API endpoint : /api/formation/videos
 * Method : POST
 * Response : String message
 * Description : Create a video
 */
export async function POST({ request, cookies }) {
	try {
		// get formData from request
		const formData = await request.formData();
		const response = await FormationService.uploadVideo(cookies, formData);
		console.log(response);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while uploading video JE SUIS UN POTI BLAGUEUR.');
			return new Response(JSON.stringify({ error: 'Internal server error.' }), {
				status: 500
			});
		}
	} catch (e) {
		console.error(e);
		return new Response(JSON.stringify({ error: 'Server error.' }), { status: 500 });
	}
}

/*
 * API endpoint : /api/formation/videos
 * Method : DELETE
 * Request : a video id
 * Response : a success message
 * Description : Delete an existing video
 */
export async function DELETE({ request, cookies }) {
	const videoId = await request.json();
	try {
		const response = await FormationService.deleteVideo(cookies, videoId);
		if (response != null) {
			return new Response(JSON.stringify(response), { status: 200 });
		} else {
			console.error('Error while deleting video.');
			return new Response(JSON.stringify({ error: 'Error while deleting video.' }), {
				status: 500
			});
		}
	} catch (e) {
		console.error(e);
		throw e;
	}
}
