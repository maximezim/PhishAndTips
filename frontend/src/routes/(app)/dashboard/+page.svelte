<script lang="ts">
  import * as Tabs from '$lib/components/ui/tabs';
  import { onMount } from 'svelte';
  import ScoringCard from '$lib/components/custom/scoring/ScoringCard.svelte';
	import FormationCard from '$lib/components/custom/formation/FormationCard.svelte';
	import ScoringBadgesCarousel from '$lib/components/custom/scoring/ScoringBadgesCarousel.svelte';
	import ScoringOsintCard from '$lib/components/custom/scoring/ScoringOsintCard.svelte';
	import ScoringPhishingCard from '$lib/components/custom/scoring/ScoringPhishingCard.svelte';
	import UsersCard from '$lib/components/custom/admin/UsersCard.svelte';

  let canGetAllUsers: boolean = false;
  let osintScore: number = 0;
  let phishingScore: number = 0;
  let formationScore: number = 0;
  let totalScore: number = 0;

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
      totalScore = await fetch("/api/scoring").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte du score total: ', e);
    }
  }

  onMount(async () => {
    canGetAllUsers = await fetch("/api/can-access/can-get-all-users").then(res => res.json());
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
    <!-- If the user is Admin, it can see its data and access to admin panel -->
    {#if canGetAllUsers}
      <Tabs.List class="grid max-w-sm grid-cols-2">
        <Tabs.Trigger value="user">Perso</Tabs.Trigger>
        <Tabs.Trigger value="admin">Admin</Tabs.Trigger>
      </Tabs.List>
    {/if}

    <!-- User tab -->
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

        <!-- Phishing scoring Card ($lib/components/custom/scoring/ScoringPhishingCard.svelte) -->
        <ScoringPhishingCard />
      </div>
    </Tabs.Content>
  
    <!-- Admin tab -->
    <Tabs.Content value="admin" >
      <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-10 w-full">
        <UsersCard />
      </div>  
    </Tabs.Content>
  </Tabs.Root>
  
</main>