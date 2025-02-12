<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import fish_svg from '$lib/assets/images/fish.svg';
  import 'iconify-icon';
  import AnimatedCircularProgressBar from '$lib/components/magicUi/AnimatedCircularProgressBar.svelte';

  export let phishingDetails: any = {};

  const accentColor = 'hsl(var(--accent))';
  const greenColor = 'green';
  const yellowColor = 'yellow';
  const redColor = 'red';
  const orangeColor = 'orange';

  function parseScore(score: string) {
    if (!score) return { value: 0, max: 0 };
    const parts = score.split('/');
    return { value: parseInt(parts[0]), max: parseInt(parts[1]) };
  }

  function selectColor(value: number, max: number) {
    if (max == 0) return greenColor;
    if ((value / max) * 100 < 10) return greenColor;
    if ((value / max) * 100 < 33) return yellowColor;
    if ((value / max) * 100 < 66) return orangeColor;
    return redColor;
  }
</script>

<Card.Root class="col-span-8 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Phishing</span>
      <img src={fish_svg} alt="fish" class="w-5 h-5 ms-1 fish text-accent" />
      </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6">
    <p class="font-medium"><span class="font-semibold text-accent">{parseScore(phishingDetails['Email Opened']).max}</span> campagnes subies</p>
    <div class="flex flex-row justify-between gap-5 px-5">
      <div class="flex flex-col justify-center gap-3">
        {#if phishingDetails['Email Opened']}
          <AnimatedCircularProgressBar class="w-100 h-100" value={parseScore(phishingDetails['Email Opened']).value} min={0} max={parseScore(phishingDetails['Email Opened']).max} gaugePrimaryColor={selectColor(parseScore(phishingDetails['Email Opened']).value, parseScore(phishingDetails['Email Opened']).max)} />
        {:else}
          <AnimatedCircularProgressBar class="w-100 h-100" value={0} min={0} max={0} gaugePrimaryColor={accentColor} />
        {/if}
        <span class="text-center text-sm font-semibold">Emails ouverts</span>
      </div>
      <div class="flex flex-col justify-center gap-3">
        {#if phishingDetails['Clicked Link']}
          <AnimatedCircularProgressBar class="w-100 h-100" value={parseScore(phishingDetails['Clicked Link']).value} min={0} max={parseScore(phishingDetails['Clicked Link']).max} gaugePrimaryColor={selectColor(parseScore(phishingDetails['Clicked Link']).value, parseScore(phishingDetails['Clicked Link']).max)} />
        {:else}
          <AnimatedCircularProgressBar class="w-100 h-100" value={0} min={0} max={0} gaugePrimaryColor={accentColor} />
        {/if}
        <span class="text-center text-sm font-semibold">Liens cliqués</span>
      </div>
      <div class="flex flex-col justify-center gap-3">
        {#if phishingDetails['Submitted Data']}
          <AnimatedCircularProgressBar class="w-100 h-100" value={parseScore(phishingDetails['Submitted Data']).value} min={0} max={parseScore(phishingDetails['Submitted Data']).max} gaugePrimaryColor={selectColor(parseScore(phishingDetails['Submitted Data']).value, parseScore(phishingDetails['Submitted Data']).max)} />
        {:else}
          <AnimatedCircularProgressBar class="w-100 h-100" value={0} min={0} max={0} gaugePrimaryColor={accentColor} />
        {/if}
        <span class="text-center text-sm font-semibold">Informations envoyées</span>
      </div>
    </div>
  </Card.Content>
</Card.Root>