<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
  import * as Select from "$lib/components/ui/select";
	import type { User, UserWithoutRole } from "$types/users";
	import { Input } from "$lib/components/ui/input";
	import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
  import { showSuccessToast, showErrorToast } from "$lib/toast";
  
  let user: UserWithoutRole = {
    firstName: "",
    lastName: "",
    email: "",
    position: "",
  };

  let errors = {
    firstName: "",
    lastName: "",
    email: "",
    position: "",
  };

  function validateForm(): boolean {
    let isValid = true;

    if (!user.firstName.trim()) {
      errors.firstName = "Le prénom est obligatoire.";
      isValid = false;
    } else {
      errors.firstName = "";
    }

    if (!user.lastName.trim()) {
      errors.lastName = "Le nom est obligatoire.";
      isValid = false;
    } else {
      errors.lastName = "";
    }

    if (!user.email.trim()) {
      errors.email = "L'adresse email est obligatoire.";
      isValid = false;
    } else {
      errors.email = "";
    }

    if (!user.position.trim()) {
      errors.position = "La position est obligatoire.";
      isValid = false;
    } else {
      errors.position = "";
    }

    return isValid;
  }

  async function createUser() {
    if (!validateForm()){
      console.error(errors);
      return;
    }

    try{
      await fetch('/api/db/user', {
        method: 'POST',
        body: JSON.stringify(user),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      sessionStorage.setItem("showSuccessToast", "Utilisateur créé avec succès");
    } catch(e) {
      console.error('Error while creating user: ', e);
      sessionStorage.setItem("showSuccessToast", "Une erreur s'est produite lors de la création de l'utilisateur");
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
    <Button class={"bg-accent py-0 px-3 text-accent-foreground flex flex-row align-middle gap-1"} builders={[builder]}>
      Ajouter
      <iconify-icon class="icon-custom" icon="mingcute:add-circle-line"></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Ajouter un utilisateur</AlertDialog.Title>
    </AlertDialog.Header>
    <div class="grid grid-cols-1 p-1 w-full gap-x-8 gap-y-4 pt-5 overflow-auto">
      <!-- First Name -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer un prénom</p>
        <Input type="text" bind:value={user.firstName} placeholder="Prénom de l'utilisateur" class="w-full" />
        {#if errors.firstName}
          <p class="text-red-500 text-sm">{errors.firstName}</p>
        {/if}
      </div>

      <!-- Last Name -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer un nom</p>
        <Input type="text" bind:value={user.lastName} placeholder="Nom de l'utilisateur" class="w-full" />
        {#if errors.lastName}
          <p class="text-red-500 text-sm">{errors.lastName}</p>
        {/if}
      </div>

      <!-- Email address -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer une adresse email</p>
        <Input type="text" bind:value={user.email} placeholder="Email de l'utilisateur" class="w-full" />
        {#if errors.email}
          <p class="text-red-500 text-sm">{errors.email}</p>
        {/if}
      </div>

      <!-- Position -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer une position</p>
        <Input type="text" bind:value={user.position} placeholder="Position de l'utilisateur" class="w-full" />
        {#if errors.position}
          <p class="text-red-500 text-sm">{errors.position}</p>
        {/if}
      </div>
    </div>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création de l'utilisateur" name="Ajouter" style="bg-accent" functionToCall={createUser} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>