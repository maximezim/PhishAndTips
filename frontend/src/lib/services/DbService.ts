import axios from 'axios';
import AuthService from './AuthService';
import type { UserWithoutRole } from '$types/users';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

class DbService {
	/*
	 * User
	 * CRUD, read-all and import CSV
	 */
	// Create
	public static async createUser(cookies: any, user: any): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/register`, user, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			return response;
		} catch (error: any) {
			console.error('Error while creating user:', error.message);
			console.error(error);
			return;
		}
	}

	// Read
	public static async getUser(cookies: any, userEmail: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/get-user?email=${userEmail}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while fetching user data:', error.message);
			console.error(error);
			return;
		}
	}

	// Update
	public static async updateUser(cookies: any, user: any): Promise<any[]> {
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
			console.error(error);
			return [];
		}
	}

	// Delete
	public static async deleteUser(cookies: any, user: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/delete-user`, user, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while deleting user:', error.message);
			console.error(error);
			return [];
		}
	}

	// Read-all
	public static async getUsers(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/get-all-users`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});

			// TODO: handle pagination
			return response.data._embedded.userDTOList;
		} catch (error: any) {
			console.error('Error while fetching users data:', error.message);
			console.error(error);
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
			console.error(error);
			return [];
		}
	}
}

export default DbService;
