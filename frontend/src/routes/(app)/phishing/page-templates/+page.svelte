<script lang='ts'>
    import 'iconify-icon';
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
	import Button from "$lib/components/ui/button/button.svelte";
    import { onMount } from 'svelte';

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
            stylePage = "grid grid-cols-[repeat(auto-fit,minmax(400px,1fr))] gap-5 my-8";
        }else{
            stylePage = "grid grid-cols-6 gap-5 my-8";
            stylePageItem = "col-span-6 lg:col-span-3 xl:col-span-2";
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


    <div class={stylePage}>
        {#each pages as page}
        <AlertDialog.Root>
            <AlertDialog.Trigger asChild let:builder>
                <div class="page flex flex-col justify-between h-72 bg-white rounded shadow-lg relative overflow-hidden p-4 {stylePageItem}">
                    <div class="w-full h-48 shadow rounded overflow-hidden">
                        <iframe srcdoc={page.html} class="w-full h-full rounded-shadow pointer-events-none" title="Redirection Model"></iframe>
                    </div>
                    <div class="info flex justify-between items-center mt-2">
                        <div class="info flex flex-col">
                            <h2 class="font-semibold">{page.name}</h2>
                            <p class="text-sm italic text-slate-400">{formatDate(page.modified_date)}</p>
                        </div>
                        <Button class="bg-accent" builders={[builder]}>Voir le modèle</Button>
                    </div>
                </div>
            </AlertDialog.Trigger>
            <AlertDialog.Content class="max-w-5xl flex flex-col">
                <AlertDialog.Header>
                    <AlertDialog.Title>Apercu du modèle</AlertDialog.Title>
                    <AlertDialog.Description>Voici un apercu du modèle qui peut être utilisé dans vos campagnes de phishing</AlertDialog.Description>
                </AlertDialog.Header>
                <div class="w-full mt-2 p-5 bg-accent/[0.07] rounded h-[60svh] overflow-y-auto">
                    <iframe srcdoc={page.html} class="w-full h-full rounded-shadow pointer-events-none" title="Redirection Model"></iframe>
                </div>
                <AlertDialog.Footer>
                    <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
                </AlertDialog.Footer>
            </AlertDialog.Content>
        </AlertDialog.Root>
            
        {/each}
    </div>

</div>