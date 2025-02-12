import Cookies from 'js-cookie';
import axios from 'axios';
import AuthService from './AuthService';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

class FormationService {
	public static async getVideos(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/formation/videos`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Erreur lors de la récupération des vidéos:', error.message);
			return [];
		}
	}

	public static async getQuizzes(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/formation/quizzes`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Erreur lors de la récupération des quizzes:', error.message);
			return [];
		}
	}

	public static async getQuizById(cookies: any, quizId: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${GATEWAY_URL}/formation/quiz/${quizId}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while fetching quiz:', error.message);
			return;
		}
	}
}

export default FormationService;
