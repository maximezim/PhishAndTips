<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import fish_svg from '$lib/assets/images/fish.svg';
  import 'iconify-icon';
  import AnimatedCircularProgressBar from '$lib/components/magicUi/AnimatedCircularProgressBar.svelte';

  const accentColor = 'hsl(var(--accent))';
</script>

<Card.Root class="col-span-8 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Phishing</span>
      <img src={fish_svg} alt="fish" class="w-5 h-5 ms-1 fish text-accent" />
      </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6">
    <p class="font-medium"><span class="font-semibold text-accent">6</span> campagnes subies</p>
    <div class="flex flex-row justify-between gap-5 px-5">
      <div class="flex flex-col justify-center gap-3">
        <AnimatedCircularProgressBar class="w-100 h-100" value={0} min={0} max={4} gaugePrimaryColor={accentColor} />
        <span class="text-center text-sm font-semibold">Attaques évitées</span>
      </div>
      <div class="flex flex-col justify-center gap-3">
        <AnimatedCircularProgressBar class="w-100 h-100" value={3} min={0} max={4} gaugePrimaryColor={accentColor} />
        <span class="text-center text-sm font-semibold">Liens cliqués</span>
      </div>
      <div class="flex flex-col justify-center gap-3">
        <AnimatedCircularProgressBar class="w-100 h-100" value={4} min={0} max={4} gaugePrimaryColor={accentColor} />
        <span class="text-center text-sm font-semibold">Informations envoyées</span>
      </div>
    </div>
  </Card.Content>
</Card.Root>