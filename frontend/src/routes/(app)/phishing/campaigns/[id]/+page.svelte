<script lang="ts">
  import { page } from '$app/stores';
  import { onMount } from 'svelte';
  import { get } from 'svelte/store';
  import CircularProgressBar from "$lib/components/magicUi/AnimatedCircularProgressBar.svelte";
  import * as Table from '$lib/components/ui/table';
  import * as Card  from '$lib/components/ui/card';
  import { spring } from "svelte/motion";
  import { Badge } from '$lib/components/ui/badge';
  import Separator from '$lib/components/custom/Separator.svelte';
  import { Button } from '$lib/components/ui/button';
  import ConfirmPopup from '$lib/components/custom/ConfirmPopup.svelte';
  import { goto } from '$app/navigation';
  import type { Campaign, CampaignSummary } from '$types/gophish';
  import type { User } from '$types/users';
  
  let campaign: Campaign;
  let campaignSummary: CampaignSummary;

  let users: User[] = [];
  
  let timelineActions = {
    opened: new Set<string>(),
    clicked: new Set<string>(),
    sent: new Set<string>(),  
  };

  let status_text = "";
  let status_bg = "";
  let loading_data = true;
  let currentPageUser = 1;
  const rowsPerPageUser = 5;
  let totalPagesUser = 1;

  onMount(async () => {
    let id = "";
    try {
      const data = get(page).data;
    if (data && data.id) {
      id = data.id;

      const campaignResponse = await fetch(`/api/phishing/campaigns/details?id=${encodeURIComponent(id)}`, {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json'
          }
      });
      campaign = await campaignResponse.json();

      const campaignResponseSummary = await fetch(`/api/phishing/campaigns/summary?id=${encodeURIComponent(id)}`, {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json'
        }
      });
      campaignSummary = await campaignResponseSummary.json();
    } else {
      console.error('ID is undefined');
    }
    } catch (error) {
      console.error("Erreur lors de la récupération des campagnes:", error);
    } finally {
      users = campaign.results.map(result => ({
        firstName: result.first_name,
        lastName: result.last_name,
        email: result.email,
        position: result.position || "",
        role: ""
      }));

      valueSent.set(campaignSummary.stats.sent);
      setTimeout(() => {
        valueOpened.set(campaignSummary.stats.opened);
      }, 300);
      setTimeout(() => {
        valueClicked.set(campaignSummary.stats.clicked);
      }, 600);
      setTimeout(() => {
        valueSubmittedData.set(campaignSummary.stats.submitted_data);
      }, 900);

      campaign.timeline.forEach(event => {
        if (event.message === 'Submitted Data') {
          timelineActions.sent.add(event.email);
        } else if (event.message === 'Email Opened') {
          timelineActions.opened.add(event.email);
        } else if (event.message === 'Clicked Link') {
          timelineActions.clicked.add(event.email);
        }
      });

      loading_data = false; 
    }
  });

  async function deleteCampaign() {
    try {
      const res = await fetch(`/api/phishing/campaigns?id=${encodeURIComponent(campaign.id)}`, {
        method: 'DELETE',
        headers: { 'Content-Type': 'application/json' }
      });
      if (res.ok) {
        sessionStorage.setItem("showSuccessToast", "Campagne supprimée avec succès");
        goto('/phishing/campaigns');
      } else {
        sessionStorage.setItem("showErrorToast", "Erreur lors de la suppression de la campagne");
      }
    } catch (error) {
      console.error("Erreur lors de la suppression :", error);
      sessionStorage.setItem("showErrorToast", "Erreur lors de la suppression de la campagne");
    }
  }

  async function completeCampaign() {
    try {
      const res = await fetch(`/api/phishing/campaigns/${campaign.id}/complete`, {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' }
      });
      if (res.ok) {
        sessionStorage.setItem("showSuccessToast", "Campagne terminée avec succès");
        location.reload();
      } else {
        sessionStorage.setItem("showErrorToast", "Erreur lors de la complétion de la campagne");
      }
    } catch (error) {
      console.error("Erreur lors de la complétion :", error);
      sessionStorage.setItem("showErrorToast", "Erreur lors de la complétion de la campagne");
    }
  }

  function changePageUser(page: number) {
      if (page >= 1 && page <= totalPagesUser) {
          currentPageUser = page;
      }
  }
  function getCurrentPageRowsUser() {
      const start = (currentPageUser - 1) * rowsPerPageUser;
      const end = start + rowsPerPageUser;
      return users.slice(start, end);
  }

  function formatDate(dateString: string): string {
    const date = new Date(dateString);
    const months = [
      "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
      "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre"
    ];
    const day = date.getDate();
    const month = months[date.getMonth()];
    const year = date.getFullYear();
    return `${day} ${month} ${year}`;
  }

  function formatStatus(statut: string): string {
  switch (statut.toLowerCase()) {
      case "created":
        status_text = "text-purple-700";
        status_bg = "bg-purple-50";
        return "Créée";
      case "queued":
        status_text = "text-blue-700";
        status_bg = "bg-blue-50";
        return "En file d'attente";
      case "in progress":
        status_text = "text-yellow-700";
        status_bg = "bg-yellow-50";
        return "En cours";
      case "completed":
        status_text = "text-green-700";
        status_bg = "bg-green-50";
        return "Terminée";
      case "failed":
        status_text = "text-red-700";
        status_bg = "bg-red-50";
        return "Échouée";
      case "paused":
        status_text = "text-gray-700";
        status_bg = "bg-gray-50";
        return "En pause";
      case "cancelled":
        status_text = "text-amber-700";
        status_bg = "bg-amber-50";
        return "Annulée";
      case "draft":
        status_text = "text-gray-700";
        status_bg = "bg-gray-50";
        return "Brouillon";
      default:

        return "Inconnu";
  }
  }

  let valueSent = spring(0, {
    stiffness: 0.03,
    damping: 0.9,
  });
  let valueOpened = spring(0, {
    stiffness: 0.03,
    damping: 0.9,
  });
  let valueClicked = spring(0, {
    stiffness: 0.03,
    damping: 0.9,
  });
  let valueSubmittedData = spring(0, {
    stiffness: 0.03,
    damping: 0.9,
  });

  function getUserActions(userEmail: string): { sent: boolean, opened: boolean, clicked: boolean } {
    return {
      sent: timelineActions.sent.has(userEmail),
      opened: timelineActions.opened.has(userEmail),
      clicked: timelineActions.clicked.has(userEmail),
    };
  }

  </script>

{#if !loading_data}
<div class="relative z-10 flex flex-col w-full py-5 px-5 sm:py-6 sm:px-8">
  <div class="header flex items-center justify-between gap-4">
    <h1 class="text-xl font-semibold">Informations sur la campagne</h1>
    <div class="flex gap-2">
      {#if campaign.status !== "Completed"}
        <ConfirmPopup name="Terminer la campagne" description="Fin de la campagne" style="bg-accent" functionToCall={completeCampaign} />
      {/if}
      <ConfirmPopup name="Supprimer la campagne" description="Suppression de la campagne" type="destructive" functionToCall={deleteCampaign} />
    </div>
  </div>
  <div class="grid grid-cols-3 gap-6 my-6">
    <div class="col-span-3 lg:col-span-1 flex flex-col gap-2 bg-muted rounded p-5 shadow">
      <span >Nom de la campagne</span>
      <span class="font-semibold text-lg py-4 px-6 bg-violet-100 rounded">{campaignSummary.name}</span>
    </div>
    <div class="col-span-3 lg:col-span-1  flex flex-col gap-2 bg-muted rounded p-5 shadow">
      <span>Date de création</span>
      <span class="font-semibold text-lg py-4 px-6 bg-violet-100 rounded">{formatDate(campaignSummary.created_date)}</span>
    </div>
    <div class="col-span-3 lg:col-span-1  flex flex-col gap-2 bg-muted rounded p-5 shadow">
      <span>Statut</span>
      <span class="font-semibold text-lg py-4 px-6 {status_text} {status_bg} rounded">{formatStatus(campaignSummary.status)}</span>
    </div>
  </div>


  <div class="grid grid-cols-4 gap-6 mb-6">
    <div class="col-span-4 lg:col-span-2 xl:col-span-1 flex flex-col items-center gap-8 bg-muted rounded p-5 shadow">
      <div class="info_header w-full flex justify-between items-center">
        <span >Mails envoyés</span>
        <span class="font-semibold text-xl px-4 py-1 bg-violet-200 rounded">{campaignSummary.stats.sent}</span>
      </div>
      {#key $valueSent}
      <CircularProgressBar
        max={campaignSummary.stats.total}
        min={0}
        value={$valueSent}
        class="mb-4"
        gaugePrimaryColor="rgb(145, 131, 236)"
        gaugeSecondaryColor="rgba(0, 50, 100, 0.1)"
      />
      {/key}
    </div>

    <div class="col-span-4 lg:col-span-2 xl:col-span-1 flex flex-col items-center gap-8 bg-muted rounded p-6 shadow">
      <div class="info_header w-full flex justify-between items-center">
        <span >Mails ouverts</span>
        <span class="font-semibold text-xl px-4 py-1 bg-violet-200 rounded">{campaignSummary.stats.opened}</span>
      </div>
      {#key $valueOpened}
      <CircularProgressBar
        max={campaignSummary.stats.total}
        min={0}
        value={$valueOpened}
        class="mb-4"
        gaugePrimaryColor="rgb(145, 131, 236)"
        gaugeSecondaryColor="rgba(0, 50, 100, 0.1)"
      />
      {/key}
    </div>

    <div class="col-span-4 lg:col-span-2 xl:col-span-1 flex flex-col items-center gap-8 bg-muted rounded p-6 shadow">
      <div class="info_header w-full flex justify-between items-center">
        <span >Redirections</span>
        <span class="font-semibold text-xl px-4 py-1 bg-violet-200 rounded">{campaignSummary.stats.clicked}</span>
      </div>
      {#key $valueClicked}
      <CircularProgressBar
        max={campaignSummary.stats.total}
        min={0}
        value={$valueClicked}
        class="mb-4"
        gaugePrimaryColor="rgb(145, 131, 236)"
        gaugeSecondaryColor="rgba(0, 50, 100, 0.1)"
      />
      {/key}
    </div>

    <div class=" col-span-4 lg:col-span-2 xl:col-span-1 flex flex-col items-center gap-8 bg-muted rounded p-6 shadow">
      <div class="info_header w-full flex justify-between items-center">
        <span >Données envoyées</span>
        <span class="font-semibold text-xl px-4 py-1 bg-violet-200 rounded">{campaignSummary.stats.submitted_data}</span>
      </div>
      {#key $valueSubmittedData}
      <CircularProgressBar
        max={campaignSummary.stats.total}
        min={0}
        value={$valueSubmittedData}
        class="mb-4"
        gaugePrimaryColor="rgb(145, 131, 236)"
        gaugeSecondaryColor="rgba(0, 50, 100, 0.1)"
      />
      {/key}
    </div>
  </div>

  <div class="models grid grid-cols-11 gap-6">
    <div class="model col-span-11 lg:col-span-6 h-96 flex flex-col gap-4 bg-muted rounded p-6 shadow overflow-y-auto">
      <span >Modèle de redirection</span>
      <iframe srcdoc={campaign.page.html} class="w-full h-full bg-white shadow rounded p-3" title="Redirection Model"></iframe>
    </div>
    <div class="model col-span-11 lg:col-span-5 h-96 flex flex-col gap-4 bg-muted rounded p-6 shadow">
      <span >Modèle de mail</span>
      <iframe srcdoc={campaign.template.html} class="w-full h-full bg-white shadow rounded p-3" title="Redirection Model"></iframe>
    </div>
  </div>

  <div class="users p-6 bg-muted mt-6 rounded shadow">
  <Card.Root class="w-full flex flex-col">
    <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
      <Card.Title class="text-lg font-semibold flex items-center gap-3 h-10">
          <iconify-icon class="text-3xl" icon="mingcute:user-3-fill"></iconify-icon>
          Utilisateurs
      </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-grow flex-col justify-between">
      <Table.Root class="max-h-60 bg-accent/[0.03]">
          <Table.Header>
            <Table.Row>
              <Table.Head>Utilisateur</Table.Head>
              
              <Table.Head class="hidden sm:table-cell">Ouvert</Table.Head>
              <Table.Head class="hidden sm:table-cell">Cliqué</Table.Head>
              <Table.Head class="hidden sm:table-cell">Envoyé</Table.Head>
              
              <Table.Head class="text-right">Email</Table.Head>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {#each getCurrentPageRowsUser() as row}
              <Table.Row>
                <Table.Cell>
                  <div class="font-medium">{row.lastName} {row.firstName}</div>
                  <div class="text-muted-foreground hidden text-sm md:inline">{row.email}</div>
                </Table.Cell>
                <Table.Cell class="hidden sm:table-cell">
                  {#if getUserActions(row.email).opened || getUserActions(row.email).clicked}
                    <Badge class="text-xs" variant="destructive">Oui</Badge>
                  {:else}
                    <Badge class="text-xs" variant="secondary">Non</Badge>
                  {/if}
                </Table.Cell>
                <Table.Cell class="hidden sm:table-cell">
                  {#if getUserActions(row.email).clicked}
                    <Badge class="text-xs" variant="destructive">Oui</Badge>
                  {:else}
                    <Badge class="text-xs" variant="secondary">Non</Badge>
                  {/if}
                </Table.Cell>
                <Table.Cell class="hidden sm:table-cell">
                  {#if getUserActions(row.email).sent}
                    <Badge class="text-xs" variant="destructive">Oui</Badge>
                  {:else}
                    <Badge class="text-xs" variant="secondary">Non</Badge>
                  {/if}
                </Table.Cell>
                <Table.Cell class="text-right">{row.email}</Table.Cell>
              </Table.Row>
        {/each}
      </Table.Body>
  </Table.Root>
  <div class="footer">
      <Separator width={'w-full'} margin_top={'mt-4'} margin_bottom={'mb-7'} height={'h-px'}/>
      <div class="w-full flex items-center justify-between mt-4">
          <div class="relative w-1/3">
              <Button class="bg-accent" on:click={() => changePageUser(currentPageUser - 1)} disabled={currentPageUser === 1}>Précédent</Button>
          </div>
          <div class="relative w-1/3 hidden sm:flex justify-center">
              <span class="mx-2 text-sm italic">Page {currentPageUser} sur {totalPagesUser}</span>
          </div>
          <div class="relative w-1/3 flex justify-end">
              <Button class="bg-accent" on:click={() => changePageUser(currentPageUser + 1)} disabled={currentPageUser === totalPagesUser}>Suivant</Button>
          </div>
      </div>
  </div>
  </Card.Content>
  </Card.Root>
</div>
</div>

{/if}