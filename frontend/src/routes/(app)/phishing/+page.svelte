<script lang="ts">
    import { Button } from '$lib/components/ui/button';
    import * as Card  from '$lib/components/ui/card';
    import fish_svg from '$lib/assets/images/fish.svg';
    import 'iconify-icon';
	  import * as Table from '$lib/components/ui/table';
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import NewCampaign from '$lib/components/custom/NewCampaign.svelte';
    import { Badge } from '$lib/components/ui/badge';
	  import { onMount } from 'svelte';
	  import Separator from '$lib/components/custom/Separator.svelte';
    import AuthService from "$lib/services/AuthService";
	  import { goto } from '$app/navigation';

    let campaigns: any[] = [];
    let templates: any[] = [];
    let pages: any[] = [];
    let groups: any[] = [];
    let loading_data = true;

    let queryParamsCampaign = "";
    let queryParamsModel = "";

    let currentPageGroup = 1;
    const rowsPerPageGroup = 5;
    let totalPagesGroup = 1;

    onMount(async () => {
      try {
        campaigns = await AuthService.getCampaigns(); 
        templates = await AuthService.getTemplates();
        pages = await AuthService.getPages();
        groups = await AuthService.getGroups();
      } catch (error) {
        console.error("Erreur lors de la récupération des campagnes:", error);
      } finally {
        totalPagesGroup = Math.ceil(groups.length / rowsPerPageGroup);

        queryParamsModel = new URLSearchParams({
          pages: JSON.stringify(pages),
          templates: JSON.stringify(templates)
        }).toString();

        queryParamsCampaign = new URLSearchParams({
          campaigns: JSON.stringify(campaigns)
        }).toString();

        loading_data = false; 
      }
    });


    function changePageGroup(page: number) {
        if (page >= 1 && page <= totalPagesGroup) {
            currentPageGroup = page;
        }
    }

    function getCurrentPageRowsGroup() {
        const start = (currentPageGroup - 1) * rowsPerPageGroup;
        const end = start + rowsPerPageGroup;
        return groups.slice(start, end);
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

    function editGroup(id: string) {
      return () => {
        goto(`/phishing/editgroup/${id}`);
      };
    }

</script>

<main class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-8 md:p-8">

    <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-10">
      <Card.Root class="col-span-5 row-span-2">
        <Card.Header class="flex flex-col gap-3 space-y-0">
          <Card.Title class="text-lg font-semibold flex items-center gap-3">
            <img src={fish_svg} alt="fish" class="w-6 h-6"/>
            Campagnes
            </Card.Title>
        </Card.Header>
        <Card.Content>
            {#if campaigns.length > 0}
                <div class="w-full flex gap-4 overflow-x-auto">
                {#if loading_data}
                    <p class="text-muted">Chargement des campagnes...</p>
                {:else}
                  {#each campaigns as campagne}
                      <a href="/phishing/campaigns/{campagne.id}">
                          <div class="relative w-64 h-32 bg-accent/[0.1] cursor-pointer rounded flex items-center justify-center shrink-0">
                              <p class="absolute bottom-3 left-4 text-sm text-gray-700 font-semibold">{campagne.name}</p>
                          </div>
                      </a>
                  {/each}
                {/if}
                </div>
            {:else}
                <p class="text-muted">Aucune campagne</p>
            {/if}
        </Card.Content>
        <Card.Footer class="flex justify-end items-center gap-3">
            <Button variant="outline" href={`/phishing/campaigns?${queryParamsCampaign}`}>Voir toutes les campagnes</Button>
            <NewCampaign 
                groups={groups}
                pages={pages}
                templates={templates}
            />
        </Card.Footer>
      </Card.Root>

      <Card.Root class="col-span-5 row-span-2">
        <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
            <Card.Title class="text-lg font-semibold flex items-center gap-3">
                <iconify-icon class="text-3xl" icon="mingcute:web-fill"></iconify-icon>
                Modèles
            </Card.Title>
        </Card.Header>
        <Card.Content>
            <div class="w-full flex gap-4 overflow-x-auto">
                {#each pages as page}
                    <AlertDialog.Root>
                        <AlertDialog.Trigger asChild let:builder>
                            <div class="relative w-64 h-32 bg-accent/[0.1] cursor-pointer rounded flex items-center justify-center shrink-0">
                                <p class="absolute bottom-3 left-4 text-sm text-gray-700 font-semibold">{page.name}</p>
                                <Button class="w-full h-full opacity-0" builders={[builder]}>Voir le modèle</Button>
                            </div>
                        </AlertDialog.Trigger>
                        <AlertDialog.Content class="max-w-5xl flex flex-col">
                            <AlertDialog.Header>
                                <AlertDialog.Title>Apercu du modèle</AlertDialog.Title>
                                <AlertDialog.Description>Voici un apercu de modèle qui peut être utilisé dans vos campagnes de phishing</AlertDialog.Description>
                            </AlertDialog.Header>
                            <div class="w-full mt-2 p-5 bg-accent/[0.07] shadow rounded h-[60svh] overflow-y-auto">
                                <iframe srcdoc={page.html} class="w-full h-full rounded-shadow" title="Redirection Model"></iframe>
                            </div>
                            <AlertDialog.Footer>
                                <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
                            </AlertDialog.Footer>
                        </AlertDialog.Content>
                    </AlertDialog.Root>

                {/each}
            </div>
        </Card.Content>
        <Card.Footer class="flex justify-end items-center gap-3">
            <Button variant="outline" href={`phishing/model?${queryParamsModel}`}>Voir touts les modèles</Button>
        </Card.Footer>
      </Card.Root>

 

      <Card.Root class="col-span-10 row-span-2 flex flex-col">
        <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
          <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between w-full h-10">
            <div class="title_left flex gap-3">
                <iconify-icon class="text-3xl" icon="mingcute:group-3-fill"></iconify-icon>
                Groupes
            </div>
            <div class="title_right">
              <Button class="bg-accent" href="/phishing/addgroup">
                <iconify-icon class="text-2xl" icon="mingcute:add-circle-line"></iconify-icon>
              </Button>
            </div>
        </Card.Title>
        </Card.Header>
        <Card.Content class="flex flex-grow flex-col justify-between">
            <Table.Root class="max-h-60">
                <Table.Header>
                  <Table.Row>
                    <Table.Head>Groupe</Table.Head>
                    <Table.Head class="hidden sm:table-cell">Date</Table.Head>
                    <Table.Head class="hidden sm:table-cell">Nombre d'utilisateurs</Table.Head>
                    <Table.Head class="text-right">Action</Table.Head>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  {#each getCurrentPageRowsGroup() as group}
                    <Table.Row>
                      <Table.Cell>
                        <span class="font-medium">{group.name}</span>
                        <span class="text-muted-foreground hidden text-sm md:inline">{group.date}</span>
                      </Table.Cell>
                      <Table.Cell>
                        <span>{formatDate(group.modified_date)}</span>
                      </Table.Cell>
                      <Table.Cell class="hidden sm:table-cell">
                        <Badge class="text" variant="secondary">{group.num_targets} utilisateurs</Badge>
                      </Table.Cell>
                      <Table.Cell class="text-right">
                        <Button variant='outline' on:click={editGroup(group.id)}>Modifier</Button>
                      </Table.Cell>
                    </Table.Row>
                  {/each}
                </Table.Body>
            </Table.Root>
            <div class="footer">
                <Separator width={'w-full'} margin_top={'mt-4'} margin_bottom={'mb-7'} height={'h-px'}/>
                <div class="w-full flex items-center justify-between mt-4">
                    <div class="relative w-1/3">
                        <Button class="bg-accent" on:click={() => changePageGroup(currentPageGroup - 1)} disabled={currentPageGroup === 1}>Précédent</Button>
                    </div>
                    <div class="relative w-1/3 flex justify-center">
                        <span class="mx-2 text-sm italic">Page {currentPageGroup} sur {totalPagesGroup}</span>
                    </div>
                    <div class="relative w-1/3 flex justify-end">
                        <Button class="bg-accent" on:click={() => changePageGroup(currentPageGroup + 1)} disabled={currentPageGroup === totalPagesGroup}>Suivant</Button>
                    </div>
                </div>
            </div>
        </Card.Content>
      </Card.Root>

    

 
    </div>
</main>


<style>

</style>