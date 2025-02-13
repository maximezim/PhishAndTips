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

	/**
	 * Create a video
	 */
	public static async uploadVideo(cookies: any, formData: FormData) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			console.log("I'm here");
			const response = await axios.post(`${GATEWAY_URL}/admin/formation/video/upload`, formData, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			console.log(response);

			return response.data;
		} catch (error: any) {
			console.error('Error while uploading video UGVFVI:', error.message);
			return null;
		}
	}

	/**
	 * Delete a video
	 */
	public static async deleteVideo(cookies: any, videoId: number) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			console.log('Video ID : ', videoId);
			const response = await axios.delete(`${GATEWAY_URL}/admin/formation/video/${videoId}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while deleting the video:', error.message);
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
			console.log('Score envoyé : ', score);
			const requestJson = JSON.stringify({ quizId: quizId, score: score });
			const response = await axios.post(`${GATEWAY_URL}/formation/quiz/score`, requestJson, {
				headers: {
					Authorization: `Bearer ${jwt}`,
					'Content-Type': 'application/json'
				}
			});
			console.log(response);
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
