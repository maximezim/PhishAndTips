<script lang="ts">
    import 'iconify-icon';
    import fish_svg from '$lib/assets/images/fish.svg';
    import { onMount } from 'svelte';
    import { goto } from "$app/navigation";
	import Button from "$lib/components/ui/button/button.svelte";
	import Separator from '$lib/components/custom/Separator.svelte';

    let campaigns: any[] = [];
    let campaignStyle = "";
    let campagneStyleItem = "";

    onMount(async () => {
        try{
            campaigns = await fetch("/api/phishing/campaigns").then(res => res.json());
        }catch(error){
            console.error(error);
        }finally{
            handleStyle();
        }
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

    function getStatusColor(status: string){
        switch (status.toLowerCase()) {
        case "queued":
            return "bg-blue-100";
        case "in progress":
            return "bg-yellow-100";
        case "completed":
            return "bg-green-100";
        case "failed":
            return "bg-red-100";
        default:
            return "bg-gray-100";
        }
    }

    function getStatusText(status: string){
        switch (status.toLowerCase()) {
        case "queued":
            return "En attente";
        case "in progress":
            return "En cours";
        case "completed":
            return "Terminée";
        case "failed":
            return "Echouée";
        default:
            return "Inconnu";
        }
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
        {#each [...campaigns].reverse() as campaign}
        <div class="campaign flex flex-col justify-between h-72 bg-white rounded shadow-lg relative overflow-hidden p-4 {campagneStyleItem}">
            <div class="w-full h-48 shadow rounded overflow-hidden">
                <iframe srcdoc={campaign.page.html} class="w-full h-full rounded-shadow pointer-events-none" title="Redirection Model"></iframe>
                <span class="absolute rounded-full shadow top-6 right-6 px-5 py-2 {getStatusColor(campaign.status)}">
                    <p class="text-sm font-semibold">{getStatusText(campaign.status)}</p>
                  </span>
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
