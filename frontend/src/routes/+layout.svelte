<script lang="ts">
	import Header from '$lib/components/custom/header.svelte';
	import '../app.css';
	import { page } from '$app/stores';
</script>

<div class="app">
	<Header title={$page.data?.title || 'Phish&Tips'} />
	<main>
		<slot></slot>
	</main>
</div>

