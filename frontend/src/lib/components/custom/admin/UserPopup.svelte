<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
	import type { User } from "$types/users";
	import ScoringCard from "$lib/components/custom/scoring/ScoringCard.svelte";
	import FormationCard from "$lib/components/custom/./formation/FormationCard.svelte";
	import ScoringBadgesCarousel from "$lib/components/custom/scoring/ScoringBadgesCarousel.svelte";
	import ScoringOsintCard from "$lib/components/custom/./scoring/ScoringOsintCard.svelte";
	import ScoringPhishingCard from "$lib/components/custom/scoring/ScoringPhishingCard.svelte";
  
  export let user: User = {
    firstName: "",
    lastName: "",
    email: "",
    position: "",
    role: "",
  };

  let userOsintScore: number = 0;
  let userPhishingScore: number = 0;
  let userFormationScore: number = 0;
  let userTotalScore: number = 0;

  async function getUserPhishingScore() {
    try {
      userPhishingScore = await fetch("/api/scoring/phishing").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score phishing: ', e);
    }
  }

  async function getUserOsintScore() {
    try {
      userOsintScore = await fetch("/api/scoring/osint").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score osint: ', e);
    }
  }  

  async function getUserFormationScore() {
    try {
      userFormationScore = await fetch("/api/scoring/formation").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score formation: ', e);
    }
  }

  async function getUserTotalScore() {
    try {
      userTotalScore = await fetch("/api/scoring").then(res => res.json());
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte du score total: ', e);
    }
  }

  async function getUserScore() {
    try {
      await Promise.all([
        getUserPhishingScore(),
        getUserOsintScore(),
        getUserFormationScore(),
        getUserTotalScore(),
      ]);
    } catch(e) {
      console.error('Erreur lors de l\'appel de l\'API svelte de score osint: ', e);
    }
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class={"bg-accent text-xl py-0 px-3"} builders={[builder]} on:click={getUserScore}>
      <iconify-icon class="icon-custom" icon="mingcute:eye-fill" style={`color: hsl(var(--accent-foreground))`}></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-[90vw] max-h-[90vh] flex flex-col" >
    <AlertDialog.Header>
      <AlertDialog.Title>{user.firstName} {user.lastName} ({user.email})</AlertDialog.Title>
    </AlertDialog.Header>
    <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-12 w-full h-full overflow-auto">
      <!-- Scoring Card ($lib/components/custom/scoring/ScoringCard.svelte) -->
      <ScoringCard totalScore={userTotalScore} osintScore={userOsintScore} phishingScore={userPhishingScore} formationScore={userFormationScore} />
      
      <!-- Formation Card ($lib/components/custom/formation/FormationCard.svelte) -->
      <FormationCard />

      <!-- Osint scoring Card ($lib/components/custom/scoring/ScoringOsintCard.svelte) -->
      <ScoringOsintCard />

      <!-- Scoring badges carousel ($lib/components/custom/scoring/ScoringBadgesCarousel.svelte) -->
      <ScoringBadgesCarousel />

      <!-- Phishing scoring Card ($lib/components/custom/scoring/ScoringPhishingCard.svelte) -->
      <ScoringPhishingCard />
    </div>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>