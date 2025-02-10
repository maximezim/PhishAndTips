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
			return response.data;
		} catch (error: any) {
			console.error('Error while getting phishing score:', error.message);
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
			return response.data;
		} catch (error: any) {
			console.error('Error while getting formation score:', error.message);
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
			return response.data;
		} catch (error: any) {
			console.error('Error while getting total score:', error.message);
			return [];
		}
	}
}

export default ScoringService;
