<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import ScoringDiv from '$lib/components/custom/scoring/ScoringDiv.svelte';
  import Separator from "$lib/components/custom/Separator.svelte";
  import Button from "$lib/components/ui/button/button.svelte";
  
  export let osintScore: number = 0;
  export let phishingScore: number = 0;
  export let formationScore: number = 0;
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
    <ScoringDiv title="Osint" score={osintScore} />
      <Separator width="w-full" />
      <ScoringDiv title="Phishing" score={phishingScore} />
      <Separator width="w-full" />
      <ScoringDiv title="Formation" score={formationScore} />
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>