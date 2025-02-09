<script lang='ts'>
    import 'iconify-icon';
    import { onMount } from 'svelte';
    import TemplatePopup from '$lib/components/custom/phishing/TemplatePopup.svelte';
	import Separator from '$lib/components/custom/Separator.svelte';

    let pages : any[] = [];
    let stylePage = "";
    let stylePageItem = "";

    onMount(() => {
        const urlParams = new URLSearchParams(window.location.search);
        pages = JSON.parse(urlParams.get('pages') || '[]');
        handleStyle();
    });

    function handleStyle(){
        if(pages.length > 1) {
            stylePage = "grid grid-cols-1 sm:grid-cols-[repeat(auto-fit,minmax(400px,1fr))] gap-5 my-5";
        }else{
            stylePage = "grid grid-cols-1 sm:grid-cols-6 gap-5 my-5";
            stylePageItem = "col-span-1 sm:col-span-6 lg:col-span-3 xl:col-span-2";
        }
    }

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

</script>

<div class="relative z-10 flex flex-col w-full py-6 px-8">
    <div class="flex items-center gap-4">
        <iconify-icon class="text-3xl" icon="mingcute:pic-ai-fill"></iconify-icon>
        <h1 class="text-xl font-semibold">Mes modèles</h1>
    </div>

    <Separator color='bg-accent' width='w-1/5' height="h-[2.5px]" margin_top="mt-3"/>

    <div class={stylePage}>
        {#each pages as page}
            <TemplatePopup page={page} isHtmlPopup={true} styleElementItem={stylePageItem}/>
        {/each}
    </div>

</div>