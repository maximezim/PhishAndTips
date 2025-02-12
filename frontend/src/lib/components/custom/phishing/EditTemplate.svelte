<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import { Textarea } from "$lib/components/ui/textarea";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
  import type { Template } from "$types/gophish"
	import Separator from "../Separator.svelte";

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
    html: "",
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

    if (!template.html.trim()) {
      errors.html = "Le contenu HTML est obligatoire.";
      isValid = false;
    } else {
      errors.html = "";
    }

    return isValid;
  }

  async function updateTemplate() {
    if (!validateForm()){
      console.error(errors);
      return;
    } 

    template.modified_date = new Date().toISOString().slice(0, 19) + "+00:00";
    
    try{
      await fetch('/api/phishing/templates', {
        method: 'PUT',
        body: JSON.stringify(template),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      sessionStorage.setItem("showSuccessToast", "Modèle de mail modifié avec succès");
    } catch(e) {
      console.error('Error while updating template: ', e);
      sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la modification du modèle de mail");
    } finally {
      closeAlertDialog();
    }
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
      <AlertDialog.Title class="text-left">Modifiez votre modèle</AlertDialog.Title>
      <AlertDialog.Description class="text-left">
        Remplissez les informations nécessaires pour modifier votre modèle de mail.
      </AlertDialog.Description>
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-2">
        <Separator width={'w-full'} height={'h-px'}/>
        <div class="name flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un nom</p>
          <Input type="text" bind:value={template.name} placeholder="Nom du modèle" class="w-full" />
          {#if errors.templateName}
            <p class="text-red-500 text-sm text-left">{errors.templateName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un objet de mail</p>
          <Input type="text" bind:value={template.subject} placeholder="Objet du modèle" class="w-full" />
          {#if errors.subject}
            <p class="text-red-500 text-sm text-left">{errors.subject}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm text-left">Choisir un contenu HTML</p>
          <Textarea bind:value={template.html} placeholder="Contenu HTML" class="w-full min-h-[350px] max-h-[500px]" />
          {#if errors.html}
            <p class="text-red-500 text-sm text-left">{errors.html}</p>
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
