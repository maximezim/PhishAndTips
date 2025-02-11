<script lang="ts">
	import Header from '$lib/components/custom/header.svelte';
	import '$lib/../app.css';
	import { page } from '$app/stores';
	import Sidebar from '$lib/components/custom/Sidebar.svelte';
	import DotPage from '$lib/components/custom/DotPage.svelte';
	import { onMount } from 'svelte';
	import { goto } from '$app/navigation';
	import { derived } from 'svelte/store';
	import TapBar from '$lib/components/custom/TapBar.svelte';

	const mainPages = ['/about', '/dashboard', '/formation', '/osint', '/password', '/phishing'];

	export const isSubPage = derived(page, ($page) => {
    return !mainPages.includes($page.url.pathname);
	});


	var firstName: "";
	var lastName: "";
	var position: "";
	var email: "";

	onMount(async() => {
		const user = await fetch("/api/auth/get-user").then(res => res.json()); 
		firstName = user.firstName;
		lastName = user.lastName;
		position = user.position;
		email = user.email;

		if (window.location.pathname === '/') {
			goto('/dashboard');
		}
	});

</script>

<div class="app flex w-full h-screen overflow-auto">
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
	<main class="relative w-full h-full flex flex-col overflow-auto">
		<Header title={$page.data?.title || 'Phish&Tips'} firstName={firstName} lastName={lastName} position={position} email={email} isSubPage={$isSubPage} />
		<div class="relative flex-grow w-full pb-[60px] sm:pb-0 overflow-auto">
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


