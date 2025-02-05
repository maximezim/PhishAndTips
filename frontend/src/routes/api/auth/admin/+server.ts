import AuthService from '$lib/services/AuthService';

export async function GET({ cookies }) {
    try {
		const response = await AuthService.isAdmin(cookies);
        console.log('Response:', response);
        if (response) {
            console.log('Admin:', response);
            return new Response(JSON.stringify(response), { status: 200 });
        } else {
            console.error('Error: No data found');
            return new Response(JSON.stringify({ error: 'No data found' }), { status: 404 });
        }
    } catch (e) {
        console.error('Server error:', e);
        throw e;
    }
}