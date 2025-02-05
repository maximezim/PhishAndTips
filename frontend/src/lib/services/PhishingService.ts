import axios from 'axios';
import Cookies from 'js-cookie';
import AuthService from './AuthService';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;
const API_URL = import.meta.env.VITE_API_URL;
const API_KEY = import.meta.env.VITE_API_KEY;

class PhishingService {
	// CAMPAIGNS
	public static async getCampaigns(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${API_URL}/api/campaigns/?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish campaigns:', error.message);

			return [];
		}
	}

	public static async getCampaignDetails(cookies: any, campaignId: number): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(
				`${API_URL}/api/campaigns/${campaignId}?api_key=${API_KEY}`,
				{
					headers: {
						Authorization: `Bearer ${jwt}`
					}
				}
			);
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish campaign details:', error.message);
			return [];
		}
	}

	public static async getCampaignSummary(cookies: any, campaignId: number): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(
				`${API_URL}/api/campaigns/${campaignId}/summary?api_key=${API_KEY}`,
				{
					headers: {
						Authorization: `Bearer ${jwt}`
					}
				}
			);
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish campaign summary:', error.message);
			return [];
		}
	}

	public static async createCampaign(cookies: any, campaign: any) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${API_URL}/api/campaigns/?api_key=${API_KEY}`, campaign, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});

			if (response.data) {
				const body = await response.data.json();
				return body;
			}
		} catch (error: any) {
			console.error('Error while creating the Gophish campaign:', error.message);
			console.error(error);
		}
	}

	// TEMPLATES
	public static async getTemplates(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${API_URL}/api/templates/?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish templates:', error.message);
			return [];
		}
	}

	public static async createTemplate(cookies: any, template: any) {
		try {
			console.log(template);
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${API_URL}/api/templates/?api_key=${API_KEY}`, template, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});

			console.log(response);
			// if (response.data) {
			// 	const body = await response.data.json();
			// 	return body;
			// }
		} catch (error: any) {
			console.error('Error while creating the Gophish template:', error.message);
			console.error(error);
		}
	}

	// PAGES
	public static async getPages(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${API_URL}/api/pages/?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish pages:', error.message);
			return [];
		}
	}

	// GROUPS
	public static async getGroups(cookies: any): Promise<any[]> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${API_URL}/api/groups/?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish groups:', error.message);
			return [];
		}
	}

	public static async getGroupDetails(cookies: any, groupId: number): Promise<any> {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.get(`${API_URL}/api/groups/${groupId}?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while getting Gophish group details:', error.message);
			return [];
		}
	}

	public static async createGroup(cookies: any, group: any) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.post(`${API_URL}/api/groups/?api_key=${API_KEY}`, group, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});

			return response.data;
		} catch (error: any) {
			console.error('Error while creating the Gophish group:', error.message);
		}
	}

	public static async updateGroup(cookies: any, groupId: number, group: any) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.put(
				`${API_URL}/api/groups/${groupId}?api_key=${API_KEY}`,
				group,
				{
					headers: {
						Authorization: `Bearer ${jwt}`
					}
				}
			);
			return response.data;
		} catch (error: any) {
			console.error('Error while updating the Gophish group:', error.message);
		}
	}

	public static async deleteGroup(cookies: any, groupId: number) {
		try {
			const jwt = await AuthService.getTokenFromServer(cookies);
			const response = await axios.delete(`${API_URL}/api/groups/${groupId}?api_key=${API_KEY}`, {
				headers: {
					Authorization: `Bearer ${jwt}`
				}
			});
			return response.data;
		} catch (error: any) {
			console.error('Error while deleting the Gophish group:', error.message);
		}
	}
}

export default PhishingService;
