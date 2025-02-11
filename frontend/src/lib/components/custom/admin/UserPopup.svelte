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
  let userPhishingDetails: any = {};

  async function getUserPhishingScore() {
    try {
      userPhishingScore = await fetch('/api/scoring/admin/phishing', {
        method: 'POST',
        body: JSON.stringify({email: user.email}),
      }).then(res => res.json());
      userPhishingScore *= 10;
    } catch(e) {
      console.error('Error while calling svelte phishing score API: ', e);
    }
  }

  async function getUserOsintScore() {
    try {
      userOsintScore = await fetch('/api/scoring/admin/osint', {
        method: 'POST',
        body: JSON.stringify({email: user.email}),
      }).then(res => res.json());
      userOsintScore *= 10;
    } catch(e) {
      console.error('Error while calling svelte osint score API: ', e);
    }
  }  

  async function getUserFormationScore() {
    try {
      userFormationScore = await fetch('/api/scoring/admin/formation', {
        method: 'POST',
        body: JSON.stringify({email: user.email}),
      }).then(res => res.json());
      userFormationScore = (10 - userFormationScore) * 10;
    } catch(e) {
      console.error('Error while calling svelte formation score API: ', e);
    }
  }

  async function getUserTotalScore() {
    try {
      userTotalScore = await fetch('/api/scoring/admin/total', {
        method: 'POST',
        body: JSON.stringify({email: user.email}),
      }).then(res => res.json());
    } catch(e) {
      console.error('Error while calling svelte total score API: ', e);
    }
  }

  async function getPhishingDetails() {
    try {
      userPhishingDetails = await fetch('/api/scoring/admin/phishing/details', {
        method: 'POST',
        body: JSON.stringify({email: user.email}),
      }).then(res => res.json());
    } catch(e) {
      console.error('Error while calling svelte phishing details API: ', e);
    }
  }

  async function getUserScore() {
    try {
      await Promise.all([
        getUserPhishingScore(),
        getUserOsintScore(),
        getUserFormationScore(),
        getUserTotalScore(),
        getPhishingDetails(),
      ]);
    } catch(e) {
      console.error('Error while calling scoring svelte API: ', e);
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
      <ScoringPhishingCard phishingDetails={userPhishingDetails} />
    </div>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>