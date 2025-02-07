<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import 'iconify-icon';
	import ScoringPopup from '$lib/components/custom/scoring/ScoringPopup.svelte';
  import ScoringGradientBar from '$lib/components/custom/scoring/ScoringGradientBar.svelte';

  export let totalScore: number = 0;
  export let osintScore: number = 0;
  export let phishingScore: number = 0;
  export let formationScore: number = 0;
</script>

<Card.Root class="col-span-4 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Score de vulnérabilité</span>
      <iconify-icon class="text-3xl text-accent" icon="mingcute:safe-alert-fill"></iconify-icon>
    </Card.Title>
  </Card.Header>
  <Card.Content>
    <ScoringGradientBar score={totalScore} />
  </Card.Content>
  <Card.Footer>
    <ScoringPopup osintScore={osintScore} phishingScore={phishingScore} formationScore={formationScore} />
  </Card.Footer>
</Card.Root>