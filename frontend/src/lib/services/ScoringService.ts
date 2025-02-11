import axios from 'axios';
import AuthService from './AuthService';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

class ScoringService {
	/*
	 * Phishing
	 * Get the phishing score of the user connected
	 */
	public static async getPhishingScore(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/gophish-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data.score;
		} catch (error: any) {
			console.error('Error while getting phishing score:', error.message);
			return [];
		}
	}

	/*
	 * ADMIN Phishing
	 * Get the phishing score of the user of the given email
	 */
	public static async getAdminPhishingScore(cookies: any, email: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/gophish-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			console.log('PHISHING SCORE : ', response);
			return response.data.score;
		} catch (error: any) {
			console.error('Error while getting user phishing score:', error);
			return [];
		}
	}

	/*
	 * Osint
	 * Get the osint score of the user connected
	 */
	public static async getOsintScore(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/osint-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data.osintScore;
		} catch (error: any) {
			console.error('Error while getting osint score:', error.message);
			return [];
		}
	}

	/*
	 * ADMIN Osint
	 * Get the osint score of the user of the given email
	 */
	public static async getAdminOsintScore(cookies: any, email: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/osint-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			console.log('OSINT SCORE : ', response);
			return response.data.osintScore;
		} catch (error: any) {
			console.error('Error while getting user osint score:', error.message);
			return [];
		}
	}

	/*
	 * Formation
	 * Get the formation score of the user connected
	 */
	public static async getFormationScore(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/formation-average`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data.formationAverage;
		} catch (error: any) {
			console.error('Error while getting formation score:', error.message);
			return [];
		}
	}

	/*
	 * ADMIN Formation
	 * Get the formation score of the user of the given email
	 */
	public static async getAdminFormationScore(cookies: any, email: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/formation-average`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			console.log('FORMATION SCORE : ', response);
			return response.data.formationAverage;
		} catch (error: any) {
			console.error('Error while getting user formation score:', error.message);
			return [];
		}
	}

	/*
	 * Total
	 * Get the total score of the user connected
	 */
	public static async getTotalScore(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/scoring/total-score`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});

			return response.data.totalScore;
		} catch (error: any) {
			console.error('Error while getting total score:', error.message);
			return [];
		}
	}

	/*
	 * Total
	 * Get the total score of the user connected
	 */
	public static async getAdminTotalScore(cookies: any, email: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${GATEWAY_URL}/scoring/admin/total-score`, email, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			console.log('TOTAL SCORE : ', response);
			return response.data.totalScore;
		} catch (error: any) {
			console.error('Error while getting user total score:', error.message);
			return [];
		}
	}
}

export default ScoringService;
