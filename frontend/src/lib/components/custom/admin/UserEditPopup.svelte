<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
  import * as Select from "$lib/components/ui/select";
	import type { User } from "$types/users";
	import { Input } from "$lib/components/ui/input";
	import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
	import Separator from "../Separator.svelte";
  
  export let user: User = {
    firstName: "",
    lastName: "",
    email: "",
    position: "",
    role: "",

  };

  let selected = { value: user.role, label: user.role };

  $:selected;

  let errors = {
    firstName: "",
    lastName: "",
    position: "",
    selectedRole: "",
  };

  let roles:string[] = [""];

  async function getRoles(): Promise<void> {
    try {
      roles = await fetch("/api/db/roles").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de roles: ', e);
    }
  }

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

    if (!user.position.trim()) {
      errors.position = "La position est obligatoire.";
      isValid = false;
    } else {
      errors.position = "";
    }

    if (!user.role.trim()) {
      errors.selectedRole = "Le rôle est obligatoire.";
      isValid = false;
    } else {
      errors.selectedRole = "";
    }

    return isValid;
  }

  async function resetPassword(){
    try{
      await fetch('/api/db/user/reset-password', {
        method: 'POST',
        body: JSON.stringify(user.email),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      sessionStorage.setItem("showSuccessToast", "Le mot de passe de l'utilisateur a été réinitialisé avec succès");
    } catch(e) {
      console.error('Erreur lors de la réinitialisation du mot de passe: ', e);
      sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la réinitialisation du mot de passe de l'utilisateur");
    } finally {
      closeAlertDialog();
    }
  }

  async function updateUser() {
    if (!validateForm()){
      console.error(errors);
      return;
    }

    try{
      await fetch('/api/db/user', {
        method: 'PUT',
        body: JSON.stringify(user),
        headers: {
          'Content-Type': 'application/json'
        }
      });
      sessionStorage.setItem("showSuccessToast", "Utilisateur modifié avec succès");
    } catch(e) {
      console.error('Erreur lors de la modification de l\'utilisateur: ', e);
      sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la modification de l'utilisateur");
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
    <Button class={"bg-accent text-xl py-0 px-3"} builders={[builder]} on:click={getRoles}>
      <iconify-icon class="icon-custom" icon="mingcute:pencil-fill" style={`color: hsl(var(--accent-foreground))`}></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
    
    <AlertDialog.Header class="flex flex-row items-center justify-between">
      <AlertDialog.Title>{user.firstName} {user.lastName} ({user.email})</AlertDialog.Title>
      
    </AlertDialog.Header>

    <Separator width="w-full" />

    <div class="grid grid-cols-1 w-full p-1 gap-x-8 gap-y-4 pb-4 overflow-auto">
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

      <!-- Position -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer une position</p>
        <Input type="text" bind:value={user.position} placeholder="Position de l'utilisateur" class="w-full" />
        {#if errors.position}
          <p class="text-red-500 text-sm">{errors.position}</p>
        {/if}
      </div>

      <!-- Role -->
      <div class="group flex flex-col gap-2">
        <p class="text-sm">Choisir un rôle</p>
        <Select.Root bind:selected onSelectedChange={(value) => (user.role = value?.value as string)}>
          <Select.Trigger>
            <Select.Value placeholder="Rôle de l'utilisateur" />
          </Select.Trigger>
          <Select.Content>
            {#each roles as role}
              <Select.Item value={role}>{role}</Select.Item>
            {/each}
          </Select.Content>
        </Select.Root>
        {#if errors.selectedRole}
          <p class="text-red-500 text-sm">{errors.selectedRole}</p>
        {/if}
      </div>
    </div>
    
    <AlertDialog.Footer class="flex justify-between sm:justify-between">
      <ConfirmPopup description="Réinitialisation du mot de passe" name="Réinitialiser le mot de passe" style="bg-accent" functionToCall={resetPassword}/>
      <div class="flex gap-3">
        <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
        <ConfirmPopup description="Modification de l'utilisateur" name="Modifier" style="bg-accent" functionToCall={updateUser} />
      </div>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>

