<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Textarea } from "$lib/components/ui/textarea";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
  import type { Template } from "$types/gophish"

  export let template: Template = {
    id: 0,
    name: "",
    subject: "",
    text: "",
    html: "",
    modified_date: "",
  }

  let errors = {
    templateName: "",
    content: "",
    subject: "",
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!template.name.trim()) {
      errors.templateName = "Le nom du modèle est obligatoire.";
      isValid = false;
    } else {
      errors.templateName = "";
    }

    if (!template.subject.trim()) {
      errors.subject = "L'objet est obligatoire.";
      isValid = false;
    } else {
      errors.subject = "";
    }

    if (!template.text.trim() && !template.html.trim()) {
      errors.content = "Entrez au moins un contenu ou un contenu HTML.";
      isValid = false;
    } else {
      errors.content = "";
    }

    return isValid;
  }

  async function updateTemplate() {
    if (!validateForm()){
      console.error(errors);
      return;
    } 

    template.modified_date = new Date().toISOString().slice(0, 19) + "+00:00", // formatted date
    
    await fetch('/api/phishing/templates', {
			method: 'PUT',
			body: JSON.stringify(template),
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
        Remplissez les informations nécessaires pour modifier votre modèle de mail.
      </AlertDialog.Description>
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-5">
        <div class="name flex flex-col gap-2">
          <p class="text-sm">Choisir un nom</p>
          <Input type="text" bind:value={template.name} placeholder="Nom du modèle" class="w-full" />
          {#if errors.templateName}
            <p class="text-red-500 text-sm">{errors.templateName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un objet de mail</p>
          <Input type="text" bind:value={template.subject} placeholder="Objet du modèle" class="w-full" />
          {#if errors.subject}
            <p class="text-red-500 text-sm">{errors.subject}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un contenu</p>
          <Textarea bind:value={template.text} placeholder="Contenu" class="w-full min-h-[350px] max-h-[500px]" />
          {#if errors.content}
            <p class="text-red-500 text-sm">{errors.content}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un contenu HTML</p>
          <Textarea bind:value={template.html} placeholder="Contenu HTML" class="w-full min-h-[350px] max-h-[500px]" />
          {#if errors.content}
            <p class="text-red-500 text-sm">{errors.content}</p>
          {/if}
        </div>
      </div>
    </AlertDialog.Header>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Modification du mail" name="Lancer" style="bg-accent" functionToCall={updateTemplate} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
