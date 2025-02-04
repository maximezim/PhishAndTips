import Cookies from 'js-cookie';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;
const API_URL = import.meta.env.VITE_API_URL;
const API_KEY = import.meta.env.VITE_API_KEY;

class AuthService {
	public static async authenticate(email: string, password: string): Promise<string> {
		try {
			const response = await fetch(GATEWAY_URL + '/authenticate', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({ email, password })
			});

			if (response.ok) {
				const body = await response.json();
				const token = body.jwtToken;

				return token;
			}
			return '';
		} catch (error: any) {
			console.error("Erreur lors de l'authentification:", error.message);
			throw error;
		}
	}

	// Get token from client
	public static async getToken(): Promise<string> {
		if (Cookies.get('authToken')) {
			return Cookies.get('authToken') || '';
		}
		return '';
	}

	// Get token from server
	public static async getTokenFromServer(cookies: any): Promise<string> {
		const token = cookies.get('authToken');
		if (token) {
			return token;
		}
		return '';
	}

	// Check if user is logged from client
	public static async isLogged(): Promise<boolean> {
		const token = await AuthService.getToken();
		if (token) {
			try {
				const response = await fetch(GATEWAY_URL + '/test-user', {
					method: 'GET',
					headers: {
						Authorization: `Bearer ${token}`
					}
				});

				if (response.ok) {
					return true;
				}
				AuthService.deleteToken();
				return false;
			} catch (error: any) {
				console.error("Erreur lors de l'authentification:", error.message);
				throw error;
			}
		}
		return false;
	}

	// Check if user is logged from server
	public static async isLoggedFromServer(cookies: any): Promise<boolean> {
		const token = await AuthService.getTokenFromServer(cookies);
		if (token) {
			try {
				const response = await fetch(GATEWAY_URL + '/test-user', {
					method: 'GET',
					headers: {
						Authorization: `Bearer ${token}`
					}
				});

				if (response.ok) {
					return true;
				}
				await AuthService.deleteTokenFromServer(cookies);
				return false;
			} catch (error: any) {
				console.error("Erreur lors de l'authentification:", error.message);
				throw error;
			}
		}
		return false;
	}

	// Delete token from client
	public static async deleteToken(): Promise<void> {
		if (Cookies.get('authToken')) {
			Cookies.remove('authToken', { path: '/' });
		}
	}

	// Delete token from server
	public static async deleteTokenFromServer(cookies: any): Promise<void> {
		if (cookies.get('authToken')) {
			cookies.delete('authToken', { path: '/' });
		}
	}

	// Check if user need to change password from server
	public static async needChangePasswordFromServer(cookies: any): Promise<boolean> {
		const token = await AuthService.getTokenFromServer(cookies);
		if (token) {
			const response = await fetch(GATEWAY_URL + '/need-change-password', {
				method: 'GET',
				headers: {
					Authorization: `Bearer ${token}`
				}
			});
			if (response.ok) {
				const body = await response.json();
				return body;
			}
		}
		return false;
	}

	// Change password from server
	public static async changePasswordFromServer(
		cookies: any,
		currentPassword: string,
		newPassword: string
	): Promise<any> {
		const token = await AuthService.getTokenFromServer(cookies);
		console.log('Token:', token);
		try {
			const response = await fetch(GATEWAY_URL + '/change-password', {
				method: 'POST',
				headers: {
					Authorization: `Bearer ${token}`,
					'Content-Type': 'application/json'
				},
				body: JSON.stringify({ currentPassword, newPassword })
			});
			console.log('Réponse changement de mot de passe:', response);
			if (response.ok) {
				AuthService.deleteTokenFromServer(cookies);
				return true;
			}
		} catch (error: any) {
			console.error('Erreur lors du changement de mot de passe :', error.message);
			throw error;
		}
	}
}

export default AuthService;
