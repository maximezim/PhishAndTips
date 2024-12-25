<script lang="ts">
	import Header from '$lib/components/custom/header.svelte';
	import '../../app.css';
	import { page } from '$app/stores';
	import Sidebar from '$lib/components/custom/Sidebar.svelte';
	import DotPage from '$lib/components/custom/DotPage.svelte';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import TapBar from '$lib/components/custom/TapBar.svelte';

	onMount(() => {
		if (window.location.pathname === '/') {
			goto('/dashboard');
		}
	});

</script>

<div class="app flex w-full h-screen">
	<Sidebar
		className="hidden md:flex"
		dash_text={$page.data?.dash_text}
		dash_bg= {$page.data?.dash_bg}
		about_text= {$page.data?.about_text}
		about_bg= {$page.data?.about_bg}
		form_text= {$page.data?.form_text}
		form_bg= {$page.data?.form_bg}
		osint_text= {$page.data?.osint_text}
		osint_bg= {$page.data?.osint_bg}
		pwd_text= {$page.data?.pwd_text}
		pwd_bg= {$page.data?.pwd_bg}
		phishing_text= {$page.data?.phishing_text}
		phishing_bg= {$page.data?.phishing_bg}
	/>
	<main class="relative w-full h-full overflow-scroll flex flex-col">
		<Header title={$page.data?.title || 'Phish&Tips'} nom={'Robin'}/>
		<div class="relative flex-grow w-full overflow-scroll mb-[60px]">
			<DotPage />
			<slot></slot>
		</div>
		<TapBar className="md:hidden"
			dash_text={$page.data?.dash_text}
			about_text= {$page.data?.about_text}
			form_text= {$page.data?.form_text}
			osint_text= {$page.data?.osint_text}
			pwd_text= {$page.data?.pwd_text}
			phishing_text= {$page.data?.phishing_text}
		/>
	</main>
</div>
