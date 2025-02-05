<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import { Input } from "../ui/input";
  import { Textarea } from "../ui/textarea";
  import { Button } from "../ui/button";
  import type { DateValue } from "@internationalized/date";
  import ConfirmPopup from "./ConfirmPopup.svelte";


  let subject: "";
  let templateName = "";
  let text = "";
  let html = "";
  let endDate: DateValue | undefined = undefined;

  let errors = {
    templateName: "",
    text: "",
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

    if (!text.trim()) {
      errors.text = "Le texte est obligatoire.";
      isValid = false;
    } else {
      errors.text = "";
    }

    if (!html.trim()) {
      errors.html = "Le HTML est obligatoire.";
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

    const groupJson = {
      name: templateName,
      subject: subject,
      text: text,
      html: html,
      modified_date: new Date().toISOString().slice(0, 19) + "+00:00", // formatted date
      send_by_date: endDate ? endDate.toString() : null,
    };
    
    await fetch('/api/phishing/campaigns', {
			method: 'POST',
			body: JSON.stringify(groupJson),
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
    <Button class="bg-accent" builders={[builder]}>Créer un modèle</Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Créer un nouveau modèle</AlertDialog.Title>
      <AlertDialog.Description>
        Remplissez les informations nécessaires pour créer un nouveau modèle de mail.
      </AlertDialog.Description>
      <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-5">
        <div class="name flex flex-col gap-2">
          <p class="text-sm">Choisir un nom</p>
          <Input type="text" bind:value={templateName} placeholder="Nom du modèle" class="w-full" />
          {#if errors.templateName}
            <p class="text-red-500 text-sm">{errors.templateName}</p>
          {/if}
        </div>

        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un objet de mail</p>
          <Input type="text" bind:value={subject} placeholder="Objet du modèle" class="w-full" />
          {#if errors.subject}
            <p class="text-red-500 text-sm">{errors.subject}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un contenu</p>
          <Textarea bind:value={text} placeholder="Contenu" class="w-full" />
          {#if errors.text}
            <p class="text-red-500 text-sm">{errors.text}</p>
          {/if}
        </div>
          
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un contenu HTML</p>
          <Textarea bind:value={html} placeholder="Contenu HTML" class="w-full" />
          {#if errors.html}
            <p class="text-red-500 text-sm">{errors.html}</p>
          {/if}
        </div>
      </div>
    </AlertDialog.Header>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création de la campagne" name="Lancer" style="bg-accent" functionToCall={createTemplate} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
