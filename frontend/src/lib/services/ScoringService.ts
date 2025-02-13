import axios from 'axios';
import AuthService from './AuthService';
import axiosRetry from 'axios-retry';
import { getServiceLogger } from '$utils/logger';

const logger = getServiceLogger('ScoringService');

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

// Retry failed requests up to 3 times with exponential delay
axiosRetry(axios, { retries: 3, retryDelay: axiosRetry.exponentialDelay });

// Centralized error messages
const ERROR_MESSAGES = {
	noResponse: 'No response from server',
	internalError: 'An internal error occurred',
	phishingFetchFailed: 'Failed to fetch phishing score',
	phishingDetailsFetchFailed: 'Failed to fetch phishing details',
	osintFetchFailed: 'Failed to fetch osint score',
	formationFetchFailed: 'Failed to fetch formation score',
	totalFetchFailed: 'Failed to fetch total score'
};

class ScoringService {
	/*
	 * Phishing
	 * Get the phishing score of the user connected
	 * @param cookies: Cookies from the request
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getPhishingScore(cookies: any): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/gophish-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return new Response(JSON.stringify(response.data.score), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while fetching Phishing score:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.phishingFetchFailed
					}),
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

	/*
	 * ADMIN Phishing
	 * Get the phishing score of the user of the given email
	 * @param cookies: Cookies from the request
	 * @param email: Email of the user
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getAdminPhishingScore(cookies: any, email: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			email = JSON.stringify({ email: email });
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/gophish-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data.score), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching Phishing score for ${email}:`, error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.phishingFetchFailed
					}),
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

	/*
	 * Phishing Details
	 * Get the phishing details of the user
	 * @param cookies: Cookies from the request
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getPhishingDetails(cookies: any): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/gophish-actions-count`, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while fetching Phishing details:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.phishingDetailsFetchFailed
					}),
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

	/*
	 * ADMIN Phishing Details
	 * Get the phishing score of the user of the given email
	 * @param cookies: Cookies from the request
	 * @param email: Email of the user
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getAdminPhishingDetails(cookies: any, email: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			email = JSON.stringify({ email: email });
			const response = await axios.post(
				`${GATEWAY_URL}/scoring/admin/gophish-actions-count`,
				email,
				{
					headers: {
						Authorization: `Bearer ${jwt}`,
						'Content-type': 'application/json'
					}
				}
			);
			return new Response(JSON.stringify(response.data), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching Phishing details for ${email}:`, error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.phishingDetailsFetchFailed
					}),
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

	/*
	 * Osint
	 * Get the osint score of the user connected
	 * @param cookies: Cookies from the request
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getOsintScore(cookies: any): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/osint-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return new Response(JSON.stringify(response.data.osintScore), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while fetching Osint score:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.osintFetchFailed
					}),
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

	/*
	 * ADMIN Osint
	 * Get the osint score of the user of the given email
	 * @param cookies: Cookies from the request
	 * @param email: Email of the user
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getAdminOsintScore(cookies: any, email: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			email = JSON.stringify({ email: email });
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/osint-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data.osintScore), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching Osint score for ${email}:`, error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.osintFetchFailed
					}),
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

	/*
	 * Formation
	 * Get the formation score of the user connected
	 * @param cookies: Cookies from the request
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getFormationScore(cookies: any): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/formation-average`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return new Response(JSON.stringify(response.data.formationAverage), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while fetching Formation score:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.formationFetchFailed
					}),
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

	/*
	 * ADMIN Formation
	 * Get the formation score of the user of the given email
	 * @param cookies: Cookies from the request
	 * @param email: Email of the user
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getAdminFormationScore(cookies: any, email: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			email = JSON.stringify({ email: email });
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/formation-average`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data.formationAverage), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching Formation score for ${email}:`, error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.formationFetchFailed
					}),
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

	/*
	 * Total
	 * Get the total score of the user connected
	 * @param cookies: Cookies from the request
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getTotalScore(cookies: any): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/total-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return new Response(JSON.stringify(response.data.totalScore), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while fetching Total score:', error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.totalFetchFailed
					}),
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

	/*
	 * ADMIN : Total
	 * Get the total score of the user of the given email
	 * @param cookies: Cookies from the request
	 * @param email: Email of the user
	 * @return Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getAdminTotalScore(cookies: any, email: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			email = JSON.stringify({ email: email });
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/total-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data.totalScore), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching Total score for ${email}:`, error);
			if (error.response) {
				return new Response(
					JSON.stringify({
						error: error.response.data?.message || ERROR_MESSAGES.totalFetchFailed
					}),
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

export default ScoringService;
