<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
	import { Input } from "$lib/components/ui/input";
	import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";

  let usersCSV: File;
  let formData = new FormData();

  $: if (usersCSV) {
    formData = new FormData();
    formData.append("file", usersCSV);
  }

  let errors = {
    usersCSV: "",
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!usersCSV) {
      errors.usersCSV = "Veuillez sélectionner un fichier.";
      isValid = false;
    } else {
      errors.usersCSV = "";
    }

    return isValid;
  }

  async function importUsers() {
    if (!validateForm()){
      console.error(errors);
      return;
    }

    await fetch('/api/db/users', {
			method: 'POST',
			body: formData,
		});
    closeAlertDialog();
  }

  function closeAlertDialog() {
    window.location.reload();
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class={"bg-accent py-0 px-3 text-accent-foreground flex flex-row align-middle gap-1"} builders={[builder]}>
      Importer
      <iconify-icon class="icon-custom" icon="mingcute:file-import-line"></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Importer une liste d'utilisateurs</AlertDialog.Title>
    </AlertDialog.Header>
    <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-5 overflow-auto">
      <!-- CSV File -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Format accepté : CSV</p>
        <Input accept=".csv" type="file" bind:value={usersCSV} class="w-full" />
          {#if errors.usersCSV}
          <p class="text-red-500 text-sm">{errors.usersCSV}</p>
        {/if}
      </div>
    </div>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création des utilisateurs" name="Ajouter" style="bg-accent" functionToCall={importUsers} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>