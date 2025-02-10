<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
  import * as Select from "$lib/components/ui/select";
	import type { User } from "$types/users";
	import { Input } from "$lib/components/ui/input";
  
  export let user: User = {
    firstName: "",
    lastName: "",
    email: "",
    position: "",
    role: "",
  };

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

  async function updateUser() {
    if (!validateForm()){
      console.log(errors);
      return;
    }

    await fetch('/api/db/user', {
			method: 'PUT',
			body: JSON.stringify(user),
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
    <Button class={"bg-transparent hover:bg-slate-200"} builders={[builder]} on:click={getRoles}>
      <iconify-icon class="icon-custom" icon="mingcute:pencil-fill" style={`color: hsl(var(--accent))`}></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>{user.firstName} {user.lastName} ({user.email})</AlertDialog.Title>
    </AlertDialog.Header>
    <div class="grid grid-cols-1 w-full gap-x-8 gap-y-4 pt-5 overflow-auto">
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
        <Input type="text" bind:value={user.lastName} placeholder="Prénom de l'utilisateur" class="w-full" />
        {#if errors.lastName}
          <p class="text-red-500 text-sm">{errors.lastName}</p>
        {/if}
      </div>

      <!-- Position -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer une position</p>
        <Input type="text" bind:value={user.position} placeholder="Prénom de l'utilisateur" class="w-full" />
        {#if errors.position}
          <p class="text-red-500 text-sm">{errors.position}</p>
        {/if}
      </div>

      <!-- Role -->
      <div class="group flex flex-col gap-2">
        <p class="text-sm">Choisir un rôle</p>
        <Select.Root onSelectedChange={(value) => (user.role = value?.value as string)}>
          <Select.Trigger>
            <Select.Value placeholder="Rôle" />
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
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>