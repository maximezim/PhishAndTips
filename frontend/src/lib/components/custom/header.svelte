<script lang="ts">
	import Button from "../ui/button/button.svelte";
  import 'iconify-icon';
  import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
	import { goto } from "$app/navigation";
  export let title: string;
  export let firstName: string;
  export let lastName: string;
  export let position: string;
  export let email: string;
  export let isSubPage: boolean;
  import AuthService from "$lib/services/AuthService";
  import { browser } from "$app/environment";

  function nav_back() {
      if (browser) window.history.back();
  }
  

  function handleLogout() {
    AuthService.deleteToken();
    goto('/login');
  }

</script>

<header class={"w-full py-3 px-4 sticky top-0 bg-primary flex items-center justify-between z-10"}>
  <div class={"flex flex-row align-middle gap-2"}>
    {#if isSubPage}
      <button on:click={nav_back} aria-label="Go back">
        <iconify-icon class="flex flex-row align-middle icon-custom text-2xl text-primary-foreground" icon="mingcute:arrow-left-fill"></iconify-icon>
      </button>
    {/if}
    <h1 class={"text-primary-foreground text-2xl font-bold"}>{title}</h1>
  </div>  
    <DropdownMenu.Root>
        <DropdownMenu.Trigger asChild let:builder>
          <Button variant="outline" class="pe-5" builders={[builder]}>
            <iconify-icon class="me-2 text-base" icon="mingcute:user-3-fill"></iconify-icon>
            {firstName} {lastName}
        </Button>
        </DropdownMenu.Trigger>
        <DropdownMenu.Content class="w-38">
          <DropdownMenu.Label>Profil</DropdownMenu.Label>
          <DropdownMenu.Separator />
          <DropdownMenu.Label>Position : {position}</DropdownMenu.Label>
          <DropdownMenu.Label>Email : {email}</DropdownMenu.Label>
          <DropdownMenu.Separator />
          <DropdownMenu.CheckboxItem class="cursor-pointer" on:click={handleLogout}>
            Se déconnecter
          </DropdownMenu.CheckboxItem>
        </DropdownMenu.Content>
      </DropdownMenu.Root>
</header>