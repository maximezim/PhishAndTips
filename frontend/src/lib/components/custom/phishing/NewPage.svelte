<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Textarea } from "$lib/components/ui/textarea";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
	import Separator from "../Separator.svelte";
  import { Checkbox } from "$lib/components/ui/checkbox";

  let pageName = "";
  let html = "";
  let redirect_url = "";

  let errors = {
    pageName: "",
    html: "",
    redirect_url: "",
  };

  let captureCredentials = true;
  let capturePasswords = true;

  function validateForm(): boolean {
    let isValid = true;

    if (!pageName.trim()) {
      errors.pageName = "Le nom du modèle est obligatoire.";
      isValid = false;
    } else {
      errors.pageName = "";
    }

    if (!html.trim()) {
      errors.html = "Le HTML est obligatoire.";
      isValid = false;
    } else {
      errors.html = "";
    }

    if (!redirect_url.trim()) {
      errors.redirect_url = "L'URL de redirection est obligatoire.";
      isValid = false;
    } else {
      errors.redirect_url = "";
    }

    return isValid;
  }

  async function createpage() {
    if (!validateForm()){
      console.error(errors);
      return;
    } 

    const pageJson = {
      name: pageName,
      html: html,
      redirect_url: redirect_url,
      capture_credentials: captureCredentials, 
      capture_passwords: capturePasswords,     
      modified_date: new Date().toISOString().slice(0, 19) + "+00:00", // formatted date
    };
    
    await fetch('/api/phishing/pages', {
			method: 'POST',
			body: JSON.stringify(pageJson),
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
    <Button class="bg-accent w-full sm:w-auto" builders={[builder]}>Créer un modèle de page</Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
    <AlertDialog.Header>
      <AlertDialog.Title class="text-left">Créer un nouveau modèle</AlertDialog.Title>
      <AlertDialog.Description class="text-left">
        Remplissez les informations nécessaires pour créer un nouveau modèle de page.
      </AlertDialog.Description>
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4">
        <Separator width={'w-full'} margin_top={'mt-2'} margin_bottom={'mb-1'} height={'h-px'}/>
        <div class="name flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un nom</p>
          <Input type="text" bind:value={pageName} placeholder="Nom du modèle" class="w-full" />
          {#if errors.pageName}
            <p class="text-red-500 text-sm text-left">{errors.pageName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un URL de redirection</p>
          <Input type="text" bind:value={redirect_url} placeholder="https://www.example.com" class="w-full" />
          {#if errors.redirect_url}
            <p class="text-red-500 text-sm text-left">{errors.redirect_url}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un contenu HTML</p>
          <Textarea bind:value={html} placeholder="Contenu HTML" class="w-full min-h-[350px] max-h-[500px]" />
          {#if errors.html}
            <p class="text-red-500 text-sm text-left">{errors.html}</p>
          {/if}
        </div>
      </div>
    </AlertDialog.Header>
    <AlertDialog.Footer>
      <div class="flex w-full items-center justify-between">
        <div class="flex gap-4">
          <!-- Updated checkbox for credentials -->
          <div class="flex items-center gap-2">
            <Checkbox id="checkbox-credentials" bind:checked={captureCredentials} />
            <label for="checkbox-credentials" class="cursor-pointer">Capturer les données</label>
          </div>
          
          <div class="flex items-center gap-2">
            <Checkbox id="checkbox-passwords" bind:checked={capturePasswords} />
            <label for="checkbox-passwords" class="cursor-pointer">Capturer les mots de passe</label>
          </div>
        </div>
        <div class="flex gap-2">
          <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
          <ConfirmPopup description="Création du page" name="Créer" style="bg-accent" functionToCall={createpage} />
        </div>
      </div>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
