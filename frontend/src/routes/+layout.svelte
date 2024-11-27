<script lang="ts">
	import Header from '$lib/components/custom/Header.svelte';
	import '../app.css';
	import { page } from '$app/stores';
	import Sidebar from '$lib/components/custom/Sidebar.svelte';
</script>

<div class="app flex w-full h-screen">
	<Sidebar />
	<main class="flex-grow h-full overflow-scroll">
		<Header title={$page.data?.title || 'Phish&Tips'} />
		<slot></slot>
	</main>
</div>


