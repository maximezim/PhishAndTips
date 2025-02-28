<script lang="ts">
    import { Button } from '$lib/components/ui/button';
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
	import EditPage from '$lib/components/custom/phishing/EditPage.svelte';
	import EditTemplate from '$lib/components/custom/phishing/EditTemplate.svelte';
	import ConfirmPopup from '$lib/components/custom/ConfirmPopup.svelte';
    import type { Template, Page } from "$types/gophish"

    export let page: Page | null = null;
    export let template: Template | null = null;
    export let isHtmlPopup: Boolean = false;
    export let styleElementItem: String = "";

    let element: any;
    let isPage: boolean = false;
    if (page != null) {
        isPage = true;
        element = page;
    } else {
        element = template;  
    }

    async function deletePage(): Promise<void> {
        try {
            await fetch('/api/phishing/pages', {
                method: 'DELETE',
                body: JSON.stringify(element.id),
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            sessionStorage.setItem("showSuccessToast", "Modèle supprimée avec succès");
        } catch (e) {
            console.error('Error while deleting page: ', e);
            sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la suppression du modèle");
        } finally {
            window.location.reload();
        }
    }

    async function deleteTemplate(): Promise<void> {
        try{
            await fetch('/api/phishing/templates', {
                method: 'DELETE',
                body: JSON.stringify(element.id),
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            sessionStorage.setItem("showSuccessToast", "Modèle de mail supprimé avec succès");
        } catch(e) {
            console.error('Error while deleting template: ', e);
            sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la suppression du modèle de mail");
        } finally {
            window.location.reload();
        }
    }
</script>


<AlertDialog.Root>
    {#if isHtmlPopup}
        <AlertDialog.Trigger asChild let:builder>
            <div class="page flex flex-col justify-between h-72 bg-white rounded shadow-lg relative overflow-hidden p-4 {styleElementItem}">
                <div class="w-full h-48 shadow rounded overflow-hidden">
                    <iframe srcdoc={element.html} class="w-full h-full rounded-shadow pointer-events-none" title="Redirection Model"></iframe>
                </div>
                <div class="info flex justify-between items-center mt-2">
                    <div class="info flex flex-col">
                        <h2 class="font-semibold">{element.name}</h2>
                    </div>
                    <Button class="bg-accent" builders={[builder]}>Voir le modèle</Button>
                </div>
            </div>
        </AlertDialog.Trigger>
    {:else}
    <AlertDialog.Trigger asChild let:builder>
        <div class="relative w-64 h-32 bg-accent/[0.1] cursor-pointer rounded flex items-center justify-center shrink-0">
            <p class="absolute bottom-3 left-4 text-sm text-gray-700 font-semibold">{element.name}</p>
            <Button class="w-full h-full opacity-0" builders={[builder]}>Voir le modèle</Button>
        </div>
    </AlertDialog.Trigger>
    {/if}
    <AlertDialog.Content class="max-w-5xl flex flex-col">
        <AlertDialog.Header class="flex flex-row justify-between gap-3">
            <div class={"flex flex-col gap-2 text-left"}>
                <AlertDialog.Title>Apercu du modèle</AlertDialog.Title>
                <AlertDialog.Description>Voici un apercu de modèle qui peut être utilisé dans vos campagnes de phishing</AlertDialog.Description>
            </div>
            <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
        </AlertDialog.Header>
        <div class="w-full mt-2 p-5 bg-accent/[0.07] shadow rounded h-[60svh] overflow-y-auto">
            <iframe srcdoc={element.html} class="w-full h-full rounded-shadow pointer-events-none hover:pointer-events-auto" title="Redirection Model"></iframe>
        </div>
        <AlertDialog.Footer class="flex-col gap-1 sm:gap-0 sm:flex-row">
            {#if isPage	}
                <EditPage page={element}/>
            {:else}
                <EditTemplate template={element}/>
            {/if}
            <ConfirmPopup name={"Supprimer"} description={isPage ? "Suppression du modèle de page" : "Suppression du modèle de mail"} style={"bg-red-500 hover:bg-red-700"} functionToCall={isPage ? deletePage : deleteTemplate}/>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>