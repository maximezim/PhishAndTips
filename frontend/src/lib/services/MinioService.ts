import axios from 'axios';
import AuthService from './AuthService';

class MinioService {
	/*
	 * Minio asset
	 * Read
	 */
	public static async getAsset(cookies: any, assetURL: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(assetURL, {
				headers: {
					Authorization: `Bearer ${jwt}`
				},
				responseType: 'arraybuffer'
			});

			// Read the response as an array buffer
			const buffer = response.data;
			const contentType = response.headers['content-type'] || 'application/octet-stream';

			return new Response(buffer, {
				headers: { 'Content-Type': contentType }
			});
		} catch (error: any) {
			console.error('Error while getting asset from Minio:', error.message);
			return;
		}
	}
}

export default MinioService;
