<script lang="ts">
  import * as Card  from '$lib/components/ui/card';
  import fish_svg from '$lib/assets/images/fish.svg';
  import 'iconify-icon';
  import AnimatedCircularProgressBar from '$lib/components/magicUi/AnimatedCircularProgressBar.svelte';

  const accentColor = 'hsl(var(--accent))';
</script>

<main class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-8 md:p-8">

  <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-10 max-w-5xl">
    <Card.Root class="col-span-5 row-span-2">
      <Card.Header class="flex flex-col gap-3 space-y-0">
        <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
          <span>Mon score</span>
          <iconify-icon class="text-3xl text-accent" icon="mingcute:trophy-fill"></iconify-icon>
          </Card.Title>
      </Card.Header>
      <Card.Content>
      </Card.Content>
    </Card.Root>

    <Card.Root class="col-span-5 row-span-2">
      <Card.Header class="flex flex-col gap-3 space-y-0">
        <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
          <span>A suivre...</span>
          <iconify-icon class="text-3xl text-accent" icon="mingcute:book-6-fill"></iconify-icon>
          </Card.Title>
      </Card.Header>
      <Card.Content>
      </Card.Content>
    </Card.Root>

    <!-- create 10 cards -->
    {#each Array.from({ length: 3 }, (_, i) => i) as _}
      <Card.Root class="col-span-1 row-span-1 bg-accent">
        <Card.Header>
        </Card.Header>
        <Card.Content>
        </Card.Content>
      </Card.Root>
    {/each}

    {#each Array.from({ length: 7 }, (_, i) => i) as _}
      <Card.Root class="col-span-1 row-span-1 bg-muted">
        <Card.Header>
        </Card.Header>
        <Card.Content>
        </Card.Content>
      </Card.Root>
    {/each}

    <Card.Root class="col-span-10 row-span-2">
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
    
  </div>
</main>