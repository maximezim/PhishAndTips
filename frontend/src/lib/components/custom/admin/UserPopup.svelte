<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import ScoringDiv from '$lib/components/custom/scoring/ScoringDiv.svelte';
  import Separator from "$lib/components/custom/Separator.svelte";
  import Button from "$lib/components/ui/button/button.svelte";
	import { onMount } from "svelte";
	import type { User } from "$types/users";
  
  export let email: string = "";
  let user: User;

  async function getUser() {
    try {
      user = await fetch(`/api/db/user?email=${email}`).then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score osint: ', e);
    }
  }  

  onMount(async () => {
    await getUser();
  })
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class={"bg-accent"} builders={[builder]}>
      Voir le détail
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-5xl flex flex-col">
    <AlertDialog.Header>
      <AlertDialog.Title>Détail du score de vulnérabilité</AlertDialog.Title>
    </AlertDialog.Header>
    <p>
      {email} {user.email} {user.last_name} {user.first_name} 
    </p>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>