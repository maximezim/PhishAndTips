<script lang="ts">
	import '../app.css';
	import { page } from '$app/stores';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';

	onMount(() => {
		if (window.location.pathname === '/') {
			goto('/dashboard');
		}
	});

</script>

<div class="app flex w-full h-screen">
	<slot></slot>
</div>