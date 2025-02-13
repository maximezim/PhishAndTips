import axios from 'axios';
import AuthService from './AuthService';
import axiosRetry from 'axios-retry';
import logger from '$utils/logger';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

// Retry failed requests up to 3 times with exponential delay
axiosRetry(axios, { retries: 3, retryDelay: axiosRetry.exponentialDelay });

// Centralized error messages
const ERROR_MESSAGES = {
	noResponse: 'No response from server',
	internalError: 'An internal error occurred',
	fetchFailed: 'Failed to fetch data'
};

class DbService {
	/*
	 * Create user
	 * Create a new user
	 * @param cookies: Cookies from the request
	 * @param user: User object
	 * @returns Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async createUser(cookies: any, user: JSON): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/register`, user, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			return new Response(JSON.stringify(response.data.score), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error('Error while creating user:', error);
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

	/*
	 * Read user
	 * Get a user by email
	 * @param cookies: Cookies from the request
	 * @param userEmail: User email
	 * @returns Response: Response object
	 * @throws Error: Throws an error if the request fails
	 */
	public static async getUser(cookies: any, userEmail: string): Promise<Response> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/get-user?email=${userEmail}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return new Response(JSON.stringify(response.data), {
				status: 200,
				headers: { 'Content-Type': 'application/json' }
			});
		} catch (error: any) {
			logger.error(`Error while fetching user ${userEmail}:`, error);
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

	// Update
	public static async updateUser(cookies: any, user: any): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/update-user`, user, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while updating user:', error.message);

			return;
		}
	}

	// Delete
	public static async deleteUser(cookies: any, userEmail: any): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/delete-user`, userEmail, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'text/plane'
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while deleting user:', error.message);

			return;
		}
	}

	// Read-all
	public static async getUsers(cookies: any, size: number, page: number): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/get-all-users?page=${page}&size=${size}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return {
				users: response.data._embedded.userDTOList,
				page: response.data.page
			};
		} catch (error: any) {
			console.error('Error while fetching users data:', error.message);

			return [];
		}
	}

	// Import CSV
	public static async importCSV(cookies: any, usersFormData: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/import-users`, usersFormData, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while importing users CSV file:', error.message);
			return [];
		}
	}

	/*
	 * Roles
	 * Get
	 */
	public static async getRoles(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/get-roles`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Erreur lors de la récupération des utilisateurs:', error.message);

			return [];
		}
	}

	public static async resetUserPassword(cookies: any, email: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(
				`${GATEWAY_URL}/reset-password?email=${email}`,
				{},
				{
					headers: {
						Authorization: `Bearer ${jwt}`
					}
				}
			);
			return response.data;
		} catch (error: any) {
			console.error('Error while resetting user password:', error.message);
			console.error(error);
			return;
		}
	}
}

export default DbService;
