<script lang='ts'>
    import 'iconify-icon';
    import { onMount } from 'svelte';
	import TemplatePopup from '$lib/components/custom/phishing/TemplatePopup.svelte';
	import Separator from '$lib/components/custom/Separator.svelte';

    let templates : any[] = [];
    let styleTemplate = "";
    let styleTemplateItem = "";

    onMount(async () => {
        try{
            templates = await fetch("/api/phishing/templates").then(res => res.json());
        }catch(error){
            console.error(error);
        }finally{
            handleStyle();
        }
    });

    function handleStyle(){
        if(templates.length > 1) {
            styleTemplate = "grid grid-cols-1 sm:grid-cols-[repeat(auto-fit,minmax(400px,1fr))] gap-5 my-5";
        }else{
            styleTemplate = "grid grid-cols-1 sm:grid-cols-6 gap-5 my-5";
            styleTemplateItem = "col-span-1 sm:col-span-6 lg:col-span-3 xl:col-span-2";
        }
    }

</script>

<div class="relative z-10 flex flex-col w-full py-6 px-8">

    <div class="flex items-center gap-4">
        <iconify-icon class="text-3xl" icon="mingcute:mail-ai-fill"></iconify-icon>
        <h1 class="text-xl font-semibold">Mes mails</h1>
    </div>

    <Separator color='bg-accent' width='w-1/5' height="h-[2.5px]" margin_top="mt-3"/>

    <div class={styleTemplate}>
        {#each templates as template}
            <TemplatePopup template={template} isHtmlPopup={true} styleElementItem={styleTemplateItem}/>
        {/each}
    </div>

</div>