<script lang="ts">
    import 'iconify-icon';
    import fish_svg from '$lib/assets/images/fish.svg';
    import { onMount } from 'svelte';
    import { goto } from "$app/navigation";
	import Button from "$lib/components/ui/button/button.svelte";
    import { browser } from "$app/environment";
	import Separator from '$lib/components/custom/Separator.svelte';

    function nav_back() {
        if (browser) window.history.back();
    }

    let campaigns: any[] = [];
    let campaignStyle = "";
    let campagneStyleItem = "";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        campaigns = JSON.parse(urlParams.get('campaigns') || '[]');
        handleStyle();
    });

    function formatDate(dateString: string): string {
      const date = new Date(dateString);
      const months = [
        "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
      ];
      const day = date.getDate();
      const month = months[date.getMonth()];
      const year = date.getFullYear();
      return `${day} ${month} ${year}`;
    }

    function scaleHTML(html: string): string {
        //take account of the gap between the differents items
        const scale = window.innerWidth / (1920 + window.innerWidth / 12);
        return html.replace(
            /<html([^>]*)>/, // Capture la balise <html> et ses attributs éventuels
            `<html$1 style="transform: scale(${scale}); transform-origin: top left;">`
        );
    }

    function handleStyle(){
        if(campaigns.length > 1) {
            campaignStyle = "grid grid-cols-1 sm:grid-cols-[repeat(auto-fit,minmax(400px,1fr))] gap-5 my-5";
        }else{
            campaignStyle = "grid grid-cols-1 sm:grid-cols-6 gap-5 my-5";
            campagneStyleItem = "col-span-1 sm:col-span-6 lg:col-span-3 xl:col-span-2";
        }
    }

</script>

<div class="relative z-10 flex flex-col w-full py-6 px-8">
    <div class="header flex items-center gap-4">
        <img src={fish_svg} alt="fish" class="w-5 h-5"/>
        <h1 class="text-xl font-semibold">Mes campagnes</h1>
    </div>

    <Separator color='bg-accent' width='w-1/5' height="h-[2.5px]" margin_top="mt-3"/>

    <div class="{campaignStyle}">
        {#each campaigns as campaign}
        <div class="campaign flex flex-col justify-between h-72 bg-white rounded shadow-lg relative overflow-hidden p-4 {campagneStyleItem}">
            <div class="w-full h-48 shadow rounded overflow-hidden">
                <iframe srcdoc={campaign.page.html} class="w-full h-full rounded-shadow" title="Redirection Model"></iframe>
            </div>
            <div class="info flex justify-between items-center mt-2">
                <div class="info flex flex-col">
                    <h2 class="text font-semibold">{campaign.name}</h2>
                    <p class="text-sm italic text-slate-400">{formatDate(campaign.launch_date)}</p>
                </div>
                <Button on:click={() => goto(`/phishing/campaigns/${campaign.id}`)} class="bg-accent">Voir la campagne</Button>
            </div>
        </div>
        {/each}
    </div>
    

</div>
