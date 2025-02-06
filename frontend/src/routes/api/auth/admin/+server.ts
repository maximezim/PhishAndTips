import AuthService from '$lib/services/AuthService';

export async function GET({ cookies }) {
    try {
		const response = await AuthService.isAdmin(cookies);
        console.log('Admin:', response);
        return new Response(JSON.stringify(response), { status: 200 });
    } catch (e) {
        console.error('Server error:', e);
        throw e;
    }
}