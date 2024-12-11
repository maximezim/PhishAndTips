<script lang="ts">
	import Button from "../ui/button/button.svelte";
    import 'iconify-icon';
    import * as DropdownMenu from "$lib/components/ui/dropdown-menu";
	import { goto } from "$app/navigation";
    export let title: string;
    export let nom: string;

  function handleLogout() {
    goto('/login');
  }

</script>

<header class={"w-full py-3 px-4 sticky top-0 bg-primary flex items-center justify-between z-10"}>
    <h1 class={"text-primary-foreground text-2xl font-bold"}>{title}</h1>
    <DropdownMenu.Root>
        <DropdownMenu.Trigger asChild let:builder>
          <Button variant="outline" class="pe-5" builders={[builder]}>
            <iconify-icon class="me-2 text-base" icon="mingcute:user-3-fill"></iconify-icon>
            {nom}
        </Button>
        </DropdownMenu.Trigger>
        <DropdownMenu.Content class="w-38">
          <DropdownMenu.CheckboxItem class="cursor-pointer" on:click={handleLogout}>
            Se déconnecter
          </DropdownMenu.CheckboxItem>
        </DropdownMenu.Content>
      </DropdownMenu.Root>
</header>