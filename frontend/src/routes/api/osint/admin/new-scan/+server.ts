import OsintService from '$lib/services/OsintService';

export async function POST({ request, cookies }) {
    const body = await request.json();
	try {
		const response = await OsintService.userNewScan(cookies, body);
        if (response != null) {
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