<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Textarea } from "$lib/components/ui/textarea";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
  import type { Page } from "$types/gophish"

  export let page: Page = {
    id: 0,
    name: "",
    html: "",
    capture_credentials: true,
    capture_passwords: true,
    modified_date: "",
    redirect_url: "",
  }

  let errors = {
    pageName: "",
    html: "",
    redirect_url: "",
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!page.name.trim()) {
      errors.pageName = "Le nom du modèle est obligatoire.";
      isValid = false;
    } else {
      errors.pageName = "";
    }

    if (!page.html.trim()) {
      errors.html = "Le HTML est obligatoire.";
      isValid = false;
    } else {
      errors.html = "";
    }

    if (!page.redirect_url.trim()) {
      errors.redirect_url = "L'URL de redirection est obligatoire.";
      isValid = false;
    } else {
      errors.redirect_url = "";
    }

    return isValid;
  }

  async function updatePage() {
    if (!validateForm()){
      console.log(errors);
      return;
    } 

    page.modified_date = new Date().toISOString().slice(0, 19) + "+00:00", // formatted date

    await fetch('/api/phishing/pages', {
			method: 'PUT',
			body: JSON.stringify(page),
			headers: {
				'Content-Type': 'application/json'
			}
		});
    closeAlertDialog();
  }

  function closeAlertDialog() {
    window.location.reload();
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class="bg-accent" builders={[builder]}>Modifier</Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Modifiez votre modèle</AlertDialog.Title>
      <AlertDialog.Description>
        Remplissez les informations nécessaires pour modifier votre modèle de page.
      </AlertDialog.Description>
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-5">
        <div class="name flex flex-col gap-2">
          <p class="text-sm">Choisir un nom</p>
          <Input type="text" bind:value={page.name} placeholder="Nom du modèle" class="w-full" />
          {#if errors.pageName}
            <p class="text-red-500 text-sm">{errors.pageName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un URL de redirection</p>
          <Input type="text" bind:value={page.redirect_url} placeholder="https://www.example.com" class="w-full" />
          {#if errors.redirect_url}
            <p class="text-red-500 text-sm">{errors.redirect_url}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un contenu HTML</p>
          <Textarea bind:value={page.html} placeholder="Contenu HTML" class="w-full min-h-[350px] max-h-[500px]" />
          {#if errors.html}
            <p class="text-red-500 text-sm">{errors.html}</p>
          {/if}
        </div>
      </div>
    </AlertDialog.Header>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Modification de la page" name="Lancer" style="bg-accent" functionToCall={updatePage} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
