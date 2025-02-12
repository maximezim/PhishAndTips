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

	public static async setVideoWatched(cookies: any, videoId: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(
				`${GATEWAY_URL}/formation/video/watched/${videoId}`,
				{},
				{
					headers: {
						Authorization: `Bearer ${jwt}`,
						'Content-Type': 'application/json'
					}
				}
			);
			return response;
		} catch (error: any) {
			console.error('Error while setting video watched:', error.message);
			return;
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

	public static async setQuizScore(cookies: any, quizId: string, score: string): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const requestJson = JSON.stringify({ quizId: quizId, score: score });
			const response = await axios.post(`${GATEWAY_URL}/formation/quiz/score`, requestJson, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			return response;
		} catch (error: any) {
			console.error('Error while setting score:', error.message);
			return;
		}
	}

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

			return;
		}
	}
}

export default FormationService;
