import axios from 'axios';
import AuthService from './AuthService';
import axiosRetry from 'axios-retry';

import { getServiceLogger } from '$utils/logger';

const logger = getServiceLogger('OsintService');

const BUCKET_URL = import.meta.env.VITE_S3_ENDPOINT;

// Retry failed requests up to 3 times with exponential delay
axiosRetry(axios, { retries: 3, retryDelay: axiosRetry.exponentialDelay });

// Centralized error messages
const ERROR_MESSAGES = {
	noResponse: 'No response from server',
	internalError: 'An internal error occurred',
	fetchFailed: 'Failed to fetch asset'
};

class MinioService {
	/*
	 * Minio asset
	 * Read
	 */
	public static async getAsset(cookies: any, assetURL: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${BUCKET_URL}${assetURL}`, {
				headers: { Authorization: `Bearer ${jwt}` },
				responseType: 'arraybuffer'
			});

			// Read the response as an array buffer
			const buffer = response.data;
			const contentType = response.headers['content-type'] || 'application/octet-stream';

			return new Response(buffer, {
				headers: { 'Content-Type': contentType }
			});
		} catch (error: any) {
			logger.error('Error while getting asset from Minio:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({ error: error.response.data?.message || ERROR_MESSAGES.fetchFailed }),
					{ status: error.response.status, headers: { 'Content-Type': 'application/json' } }
				);
			} else if (error.request) {
				return new Response(JSON.stringify({ error: ERROR_MESSAGES.noResponse }), {
					status: 504,
					headers: { 'Content-Type': 'application/json' }
				});
			} else {
				return new Response(JSON.stringify({ error: ERROR_MESSAGES.internalError }), {
					status: 500,
					headers: { 'Content-Type': 'application/json' }
				});
			}
		}
	}
}

export default MinioService;
