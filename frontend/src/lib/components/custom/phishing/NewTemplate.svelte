<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Textarea } from "$lib/components/ui/textarea";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
	import Separator from "../Separator.svelte";


  let subject: "";
  let templateName = "";
  let html = "";

  let errors = {
    templateName: "",
    html: "",
    subject: "",
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!templateName.trim()) {
      errors.templateName = "Le nom du modèle est obligatoire.";
      isValid = false;
    } else {
      errors.templateName = "";
    }

    if (!subject.trim()) {
      errors.subject = "L'objet est obligatoire.";
      isValid = false;
    } else {
      errors.subject = "";
    }

    if (!html.trim()) {
      errors.html = "Le contenu HTML est obligatoire.";
      isValid = false;
    } else {
      errors.html = "";
    }

    return isValid;
  }

  async function createTemplate() {
    if (!validateForm()){
      console.log(errors);
      return;
    } 

    const templateJson = {
      name: templateName,
      subject: subject,
      text: "",
      html: html,
      modified_date: new Date().toISOString().slice(0, 19) + "+00:00", // formatted date
    };
    
    await fetch('/api/phishing/templates', {
			method: 'POST',
			body: JSON.stringify(templateJson),
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
    <Button class="bg-accent w-full sm:w-auto" builders={[builder]}>Créer un modèle de mail</Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
    <AlertDialog.Header>
      <AlertDialog.Title class="text-left">Créer un nouveau modèle</AlertDialog.Title>
      <AlertDialog.Description class="text-left">
        Remplissez les informations nécessaires pour créer un nouveau modèle de mail.
      </AlertDialog.Description>
      
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4">
        <Separator width={'w-full'} margin_top={'mt-2'} margin_bottom={'mb-1'} height={'h-px'}/>
        <div class="name flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un nom</p>
          <Input type="text" bind:value={templateName} placeholder="Nom du modèle" class="w-full" />
          {#if errors.templateName}
            <p class="text-red-500 text-sm text-left">{errors.templateName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un objet de mail</p>
          <Input type="text" bind:value={subject} placeholder="Objet du modèle" class="w-full" />
          {#if errors.subject}
            <p class="text-red-500 text-sm text-left">{errors.subject}</p>
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
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création du template" name="Créer" style="bg-accent" functionToCall={createTemplate} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
