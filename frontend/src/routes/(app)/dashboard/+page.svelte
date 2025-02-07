<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import fish_svg from '$lib/assets/images/fish.svg';
  import 'iconify-icon';
  import AnimatedCircularProgressBar from '$lib/components/magicUi/AnimatedCircularProgressBar.svelte';
  import * as Tabs from '$lib/components/ui/tabs';
  import { onMount } from 'svelte';
  import ScoringCard from '$lib/components/custom/scoring/ScoringCard.svelte';
	import FormationCard from '$lib/components/custom/formation/FormationCard.svelte';
	import ScoringBadgesCarousel from '$lib/components/custom/scoring/ScoringBadgesCarousel.svelte';
	import ScoringOsintCard from '$lib/components/custom/scoring/ScoringOsintCard.svelte';

  let osintScore: number = 0;
  let phishingScore: number = 0;
  let formationScore: number = 0;
  let totalScore: number = 0;

  const accentColor = 'hsl(var(--accent))';

  async function getPhishingScore() {
    try {
      phishingScore = await fetch("/api/scoring/phishing").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score phishing: ', e);
    }
  }

  async function getOsintScore() {
    try {
      osintScore = await fetch("/api/scoring/osint").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score osint: ', e);
    }
  }  

  async function getFormationScore() {
    try {
      formationScore = await fetch("/api/scoring/formation").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score formation: ', e);
    }
  }

  async function getTotalScore() {
    try {
      formationScore = await fetch("/api/scoring").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte du score total: ', e);
    }
  }

  onMount(async () => {
    await Promise.all([
      getPhishingScore(),
      getOsintScore(),
      getFormationScore(),
      getTotalScore(),
    ]);
  });
</script>

<main class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-8 md:p-8">
  <Tabs.Root value="user">
    <Tabs.List class="grid max-w-sm grid-cols-2">
      <Tabs.Trigger value="user">Perso</Tabs.Trigger>
      <Tabs.Trigger value="admin">Admin</Tabs.Trigger>
    </Tabs.List>
    <Tabs.Content value="user">
      <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-12 w-full">
        <!-- Scoring Card ($lib/components/custom/scoring/ScoringCard.svelte) -->
        <ScoringCard totalScore={totalScore} osintScore={osintScore} phishingScore={phishingScore} formationScore={formationScore} />
        
        <!-- Formation Card ($lib/components/custom/formation/FormationCard.svelte) -->
        <FormationCard />

        <!-- Osint scoring Card ($lib/components/custom/scoring/ScoringOsintCard.svelte) -->
        <ScoringOsintCard />

        <!-- Scoring badges carousel ($lib/components/custom/scoring/ScoringBadgesCarousel.svelte) -->
        <ScoringBadgesCarousel />

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
      </div>
      </Tabs.Content>
    
      <Tabs.Content value="admin" >
        <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-10 w-full">
          <Card.Root class="col-span-5 row-span-2">
            <Card.Header class="flex flex-col gap-3 space-y-0">
              <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
                <span>Score de l'entreprise</span>
                <iconify-icon class="text-3xl text-accent" icon="mingcute:trophy-fill"></iconify-icon>
                </Card.Title>
            </Card.Header>
            <Card.Content>
            </Card.Content>
          </Card.Root>

          <Card.Root class="col-span-10 row-span-2">
            <Card.Header class="flex flex-col gap-3 space-y-0">
              <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
                <span>Utilisateurs</span>
                <iconify-icon class="text-3xl text-accent" icon="mingcute:group-3-fill"></iconify-icon>
                </Card.Title>
            </Card.Header>
            <Card.Content class="flex flex-col gap-6">
              
            </Card.Content>
          </Card.Root>
        </div>  
      </Tabs.Content>
    </Tabs.Root>
  
</main>