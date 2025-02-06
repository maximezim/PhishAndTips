<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import * as Select from "$lib/components/ui/select";
  import { Input } from "$lib/components/ui/input";
  import { Button } from "$lib/components/ui/button";
  import DataPicker from "$lib/components/custom/DataPicker.svelte";
  import Label from "$lib/components/ui/label/label.svelte";
  import Checkbox from "$lib/components/ui/checkbox/checkbox.svelte";
  import type { DateValue } from "@internationalized/date";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";

  type Group = { name: string; date: string; status: string; nb: number };
  type Page = { id: number; name: string; html: string; capture_credentials: boolean; capture_passwords: boolean; modified_date: string; redirect_url: string };
  type Template = { id: number; name: string; subject: string; text: string; html: string; modified_date: string };

  export let groups: Group[] = [];
  export let templates: Template[] = [];
  export let pages: Page[] = [];

  let selectedGroup: Group | undefined = undefined;
  let selectedPage: Page | undefined = undefined;
  let selectedTemplate: Template | undefined = undefined;
  let campaignName = "";
  let launch_date: DateValue | undefined = undefined;
  let endDate: DateValue | undefined = undefined;
  let isChecked = false;

  let errors = {
    campaignName: "",
    selectedGroup: "",
    selectedPage: "",
    selectedTemplate: "",
    launchDate: "",
    endDate: ""
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!campaignName.trim()) {
      errors.campaignName = "Le nom de la campagne est obligatoire.";
      isValid = false;
    } else {
      errors.campaignName = "";
    }

    if (!selectedGroup) {
      errors.selectedGroup = "Veuillez sélectionner un groupe.";
      isValid = false;
    } else {
      errors.selectedGroup = "";
    }

    if (!selectedTemplate) {
      errors.selectedTemplate = "Veuillez sélectionner un mail.";
      isValid = false;
    } else {
      errors.selectedTemplate = "";
    }

    if (!selectedPage) {
      errors.selectedPage = "Veuillez sélectionner une page.";
      isValid = false;
    } else {
      errors.selectedPage = "";
    }

    if(!isChecked && !launch_date) {
      errors.launchDate = "Veuillez sélectionner une date de lancement.";
      isValid = false;
    } else {
      errors.launchDate = "";
    }

    if(launch_date && endDate && launch_date > endDate) {
      errors.endDate = "La date de fin doit être supérieure à la date de lancement.";
      isValid = false;
    } else {
      errors.endDate = "";
    }

    return isValid;
  }

  async function createCampaign() {
    if (!validateForm()){
      console.log(errors);
      return;
    } 

    const groupJson = {
      name: campaignName,
      template: { name: selectedTemplate?.name ?? "" },
      url: "http://localhost",
      page: { name: selectedPage?.name ?? "" },
      smtp: { name: "smtp" },
      launch_date: getFormattedDate(launch_date),
      send_by_date: endDate ? endDate.toString() : null,
      groups: [{ name: selectedGroup?.name ?? "" }],
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

  function getFormattedDate(value: DateValue | undefined): string {
    if (value) {
      const nativeDate = new Date(value.year, value.month - 1, value.day);
      return nativeDate.toISOString().slice(0, 19) + "+00:00";
    } else {
      const currentDate = new Date();
      currentDate.setSeconds(currentDate.getSeconds() + 20);
      return currentDate.toISOString().slice(0, 19) + "+00:00";
    }
  }

  function closeAlertDialog() {
    window.location.reload();
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class="bg-accent" builders={[builder]}>Créer une campagne</Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-4xl flex flex-col max-h-[90vh] overflow-y-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Créer une nouvelle campagne {getFormattedDate(launch_date)}</AlertDialog.Title>
      <AlertDialog.Description>
        Remplissez les informations nécessaires pour créer une nouvelle campagne de phishing.
      </AlertDialog.Description>
      <div class="grid grid-cols-2 w-full gap-x-8 gap-y-4 pt-5">
        <div class="name flex flex-col gap-2">
          <p class="text-sm">Choisir un nom</p>
          <Input type="text" bind:value={campaignName} placeholder="Nom de la campagne" class="w-full" />
          {#if errors.campaignName}
            <p class="text-red-500 text-sm">{errors.campaignName}</p>
          {/if}
        </div>
        <div class="group flex flex-col gap-2">
          <p class="text-sm">Choisir un groupe</p>
          <Select.Root onSelectedChange={(value) => (selectedGroup = value?.value as Group)}>
            <Select.Trigger>
              <Select.Value placeholder="Groupe" />
            </Select.Trigger>
            <Select.Content>
              {#each groups as group}
                <Select.Item value={group}>{group.name}</Select.Item>
              {/each}
            </Select.Content>
          </Select.Root>
          {#if errors.selectedGroup}
            <p class="text-red-500 text-sm">{errors.selectedGroup}</p>
          {/if}
        </div>
        <div class="mail flex flex-col gap-2">
          <p class="text-sm">Choisir un mail</p>
          <Select.Root onSelectedChange={(value) => (selectedTemplate = value?.value as Template)}>
            <Select.Trigger>
              <Select.Value placeholder="Mail" />
            </Select.Trigger>
            <Select.Content>
              {#each templates as template}
                <Select.Item value={template}>{template.name}</Select.Item>
              {/each}
            </Select.Content>
          </Select.Root>
          {#if errors.selectedTemplate}
            <p class="text-red-500 text-sm">{errors.selectedTemplate}</p>
          {/if}
        </div>
        <div class="template flex flex-col gap-2">
          <p class="text-sm">Choisir un template</p>
          <Select.Root onSelectedChange={(value) => (selectedPage = value?.value as Page)}>
            <Select.Trigger>
              <Select.Value placeholder="Template" />
            </Select.Trigger>
            <Select.Content>
              {#each pages as page}
                <Select.Item value={page}>{page.name}</Select.Item>
              {/each}
            </Select.Content>
          </Select.Root>
          {#if errors.selectedPage}
            <p class="text-red-500 text-sm">{errors.selectedPage}</p>
          {/if}
        </div>
        <div class="relative preview_mail h-48">
          <span class="flex w-full h-full rounded bg-accent/[0.1] justify-center items-center">
            {#if selectedTemplate}
              <iframe srcdoc={selectedTemplate.html} class="w-full h-full" title="Template Preview"></iframe>
            {/if}
          </span>
        </div>
        <div class="relative preview_template h-48">
          <span class="flex w-full h-full rounded bg-accent/[0.1] justify-center items-center">
            {#if selectedPage}
              <iframe srcdoc={selectedPage.html} class="w-full h-full" title="Page Preview"></iframe>
            {/if}
          </span>
        </div>
        <div class="date flex flex-col gap-2">
          <p class="text-sm">Choisir la date de lancement</p>
          <DataPicker disabled={isChecked} bind:value={launch_date} />
          <div class="now ms-1 mt-1 flex justify-start items-center gap-2">
            <Checkbox id="nowCheck" bind:checked={isChecked} />
            <Label for="nowCheck">Envoyer maintenant</Label>
          </div>
          {#if errors.launchDate}
            <p class="text-red-500 text-sm">{errors.launchDate}</p>
          {/if}
        </div>
        <div class="date flex flex-col gap-2">
          <p class="text-sm">Choisir la date de fin des envois (optionnel)</p>
          <DataPicker bind:value={endDate} />
          {#if errors.endDate}
            <p class="text-red-500 text-sm">{errors.endDate}</p>
          {/if}
        </div>
      </div>
    </AlertDialog.Header>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création de la campagne" name="Lancer" style="bg-accent" functionToCall={createCampaign} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>
