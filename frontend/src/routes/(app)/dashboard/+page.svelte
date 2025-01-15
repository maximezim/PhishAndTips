<script lang="ts">
	import AuthService from '$lib/services/AuthService';
	import { onMount } from 'svelte';

	let token = "";

	onMount(async () => {
		token = await AuthService.getToken();
	});
</script>

<h1>Token : {token}</h1>


