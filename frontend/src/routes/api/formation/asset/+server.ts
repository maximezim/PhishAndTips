import MinioService from '$lib/services/MinioService';

/*
 * API endpoint : /api/formation/asset
 * Method : GET
 * Response : An asset as an octet stream
 * Description : Get asset from minio db
 */
export async function GET({ cookies, url }) {
	try {
		const assetURL = url.searchParams.get('assetURL');
		if (!assetURL) {
			console.error('Error: assetURL is required');
			return new Response(JSON.stringify({ error: 'assetURL is required' }), { status: 400 });
		}
		const response = await MinioService.getAsset(cookies, assetURL);
		if (response) {
			return response;
		} else {
			console.error('Error while fetching asset: No asset found');
			return new Response(JSON.stringify({ error: 'No asset found' }), { status: 404 });
		}
	} catch (e) {
		console.error(e);
	}
}
