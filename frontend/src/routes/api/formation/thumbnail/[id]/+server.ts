import AuthService from '$lib/services/AuthService';
import type { RequestHandler } from '@sveltejs/kit';

const GATEWAY_URL = import.meta.env.VITE_GATEWAY_URL;

export const GET: RequestHandler = async ({ params, cookies }) => {
  const { id } = params;
  const jwt = await AuthService.getTokenFromServer(cookies);
  const assetUrl = `${GATEWAY_URL}/assets/${id}`;
  // Fetch the image with the Authorization header
  const res = await fetch(assetUrl, {
    headers: {
      'Authorization': `Bearer ${jwt}`
    }
  });

  if (!res.ok) {
    return new Response('Error fetching image', { status: res.status });
  }

  // Read the response as an array buffer
  const buffer = await res.arrayBuffer();
  const contentType = res.headers.get('Content-Type') || 'application/octet-stream';
  
  // Validate content type
  const allowedTypes = ['image/jpeg', 'image/png', 'image/gif', 'image/webp'];
  if (!allowedTypes.includes(contentType)) {
    return new Response('Invalid content type', { status: 415 });
  }
 
  return new Response(buffer, {
    headers: {
      'Content-Type': contentType,
      'Cache-Control': 'public, max-age=3600',
      'Content-Length': buffer.byteLength.toString()
    }
  });
};
