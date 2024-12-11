<script lang="ts">
    import { Button } from '$lib/components/ui/button';
    import * as Card  from '$lib/components/ui/card';
    import fish_svg from '$lib/assets/images/fish.svg';
    import 'iconify-icon';
	import * as Table from '$lib/components/ui/table';
    import NewCampaign from '$lib/components/custom/NewCampaign.svelte';
    import EditGroup from '$lib/components/custom/EditGroup.svelte';
    import { Badge } from '$lib/components/ui/badge';
	import { onMount } from 'svelte';
	import Separator from '$lib/components/custom/Separator.svelte';

    let currentPageUser = 1;
    const rowsPerPageUser = 5;
    let totalPagesUser = 1;

    let currentPageGroup = 1;
    const rowsPerPageGroup = 5;
    let totalPagesGroup = 1;

    let campaigns = [
        {name: "Campagne 1", id: 1},
        {name: "Campagne 2", id: 2},
        {name: "Campagne 3", id: 3},
        {name: "Campagne 4", id: 4},
    ]

    let templates=[
        {name: "Modèle 1"},
        {name: "Modèle 2"},
        {name: "Modèle 3"},
    ]

    let mails=[
        {name: "Mail 1"},
        {name: "Mail 2"},
        {name: "Mail 3"},
    ]

    let users = [
        { customer: "Liam Johnson", type: "Sale", status: "Fulfilled", date: "2023-06-23", amount: "$250.00", email: "liam@example.com" },
        { customer: "Emma Williams", type: "Sale", status: "Pending", date: "2023-06-24", amount: "$150.00", email: "emma@example.com" },
        { customer: "Noah Brown", type: "Sale", status: "Fulfilled", date: "2023-06-25", amount: "$300.00", email: "noah@example.com" },
        { customer: "Olivia Jones", type: "Sale", status: "Pending", date: "2023-06-26", amount: "$200.00", email: "olivia@example.com" },
        { customer: "William Garcia", type: "Sale", status: "Fulfilled", date: "2023-06-27", amount: "$400.00", email: "william@example.com" },
        { customer: "Sophia Martinez", type: "Sale", status: "Pending", date: "2023-06-28", amount: "$500.00", email: "sophia@example.com" },
        { customer: "James Rodriguez", type: "Sale", status: "Fulfilled", date: "2023-06-29", amount: "$600.00", email: "james@example.com" },
        { customer: "Isabella Hernandez", type: "Sale", status: "Pending", date: "2023-06-30", amount: "$700.00", email: "isabella@example.com" },
        { customer: "Benjamin Lopez", type: "Sale", status: "Fulfilled", date: "2023-07-01", amount: "$800.00", email: "benjamin@example.com" },
        { customer: "Mia Gonzalez", type: "Sale", status: "Pending", date: "2023-07-02", amount: "$900.00", email: "mia@example.com" },
    ]

    let groups = [
        {id: 1, name: "Groupe 1", date: "2023-06-23", status:"None", nb: 5},
        {id: 2, name: "Groupe 2", date: "2023-06-24", status:"Over",nb: 10},
        {id: 3, name: "Groupe 3", date: "2023-06-25", status:"None",nb: 15},
        {id: 4, name: "Groupe 4", date: "2023-06-26", status:"Over",nb: 20},
        {id: 5, name: "Groupe 5", date: "2023-06-27", status:"None",nb: 25},
        {id: 6, name: "Groupe 6", date: "2023-06-28", status:"None",nb: 30},
        {id: 7, name: "Groupe 7", date: "2023-06-29", status:"None",nb: 35},
    ]

    onMount(() => {
        totalPagesUser = Math.ceil(users.length / rowsPerPageUser);
        totalPagesGroup = Math.ceil(groups.length / rowsPerPageGroup);
    });

    function changePageUser(page: number) {
        if (page >= 1 && page <= totalPagesUser) {
            currentPageUser = page;
        }
    }
    function changePageGroup(page: number) {
        if (page >= 1 && page <= totalPagesGroup) {
            currentPageGroup = page;
        }
    }

    function getCurrentPageRowsUser() {
        const start = (currentPageUser - 1) * rowsPerPageUser;
        const end = start + rowsPerPageUser;
        return users.slice(start, end);
    }
    function getCurrentPageRowsGroup() {
        const start = (currentPageGroup - 1) * rowsPerPageGroup;
        const end = start + rowsPerPageGroup;
        return groups.slice(start, end);
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
                {#each campaigns as campagne}
                    <a href="/phishing/campaigns/{campagne.id}">
                        <div class="relative w-64 h-32 bg-accent/[0.1] cursor-pointer rounded flex items-center justify-center shrink-0">
                            <p class="absolute bottom-3 left-4 text-sm text-gray-700 font-semibold">{campagne.name}</p>
                        </div>
                    </a>
                {/each}
                </div>
            {:else}
                <p class="text-muted">Aucune campagne</p>
            {/if}
        </Card.Content>
        <Card.Footer class="flex justify-end items-center gap-3">
            <Button variant="outline" href="/phishing/campaigns">Voir toutes les campaigns</Button>
            <NewCampaign 
                groups={groups}
                mails={mails}
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
                {#each templates as template}
                    <div class="relative w-64 h-32 bg-accent/[0.1] cursor-pointer rounded flex items-center justify-center shrink-0">
                        <p class="absolute bottom-3 left-4 text-sm text-gray-700 font-semibold">{template.name}</p>
                    </div>
                {/each}
            </div>
        </Card.Content>
        <Card.Footer class="flex justify-end items-center gap-3">
            <Button variant="outline" href="phishing/model">Voir touts les modèles</Button>
        </Card.Footer>
      </Card.Root>

      <Card.Root class="col-span-4 row-span-2">
        <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
          <Card.Title class="text-lg font-semibold flex items-center gap-3">
            <iconify-icon class="text-3xl" icon="mingcute:group-3-fill"></iconify-icon>
            Groupes
        </Card.Title>
        </Card.Header>
        <Card.Content>
            <Table.Root class="max-h-60">
                <Table.Header>
                  <Table.Row>
                    <Table.Head>Groupe</Table.Head>
                    <Table.Head class="hidden sm:table-cell">Status</Table.Head>
                    <Table.Head class="text-right">Nombre</Table.Head>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  {#each getCurrentPageRowsGroup() as row}
                  <EditGroup group={row} />
                    {/each}
                </Table.Body>
            </Table.Root>
            <Separator width={'w-full'} margin_top={'mt-4'} margin_bottom={'mb-7'} height={'h-px'}/>
            <div class="w-full flex items-center justify-between mt-4">
                <div class="relative w-1/3">
                    <Button on:click={() => changePageGroup(currentPageGroup - 1)} disabled={currentPageGroup === 1}>Précédent</Button>
                </div>
                <div class="relative w-1/3 flex justify-center">
                    <span class="mx-2 text-sm italic">Page {currentPageGroup} sur {totalPagesGroup}</span>
                </div>
                <div class="relative w-1/3 flex justify-end">
                    <Button on:click={() => changePageGroup(currentPageGroup + 1)} disabled={currentPageGroup === totalPagesGroup}>Suivant</Button>
                </div>
            </div>
        </Card.Content>
      </Card.Root>

      <Card.Root class="col-span-6 row-span-2">
        <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
            <Card.Title class="text-lg font-semibold flex items-center gap-3">
                <iconify-icon class="text-3xl" icon="mingcute:user-3-fill"></iconify-icon>
                Utilisateurs
            </Card.Title>
        </Card.Header>
        <Card.Content>
            <Table.Root class="max-h-60">
                <Table.Header>
                  <Table.Row>
                    <Table.Head>Customer</Table.Head>
                    <Table.Head class="hidden sm:table-cell">Type</Table.Head>
                    <Table.Head class="hidden sm:table-cell">Status</Table.Head>
                    <Table.Head class="hidden md:table-cell">Date</Table.Head>
                    <Table.Head class="text-right">Email</Table.Head>
                  </Table.Row>
                </Table.Header>
                <Table.Body>
                  {#each getCurrentPageRowsUser() as row}
                    <Table.Row>
                      <Table.Cell>
                        <div class="font-medium">{row.customer}</div>
                        <div class="text-muted-foreground hidden text-sm md:inline">{row.email}</div>
                    </Table.Cell>
                    <Table.Cell class="hidden sm:table-cell">{row.type}</Table.Cell>
                    <Table.Cell class="hidden sm:table-cell">
                        <Badge class="text-xs" variant="secondary">{row.status}</Badge>
                    </Table.Cell>
                    <Table.Cell class="hidden md:table-cell">{row.date}</Table.Cell>
                    <Table.Cell class="text-right">{row.email}</Table.Cell>
                    </Table.Row>
              {/each}
            </Table.Body>
          </Table.Root>
        <div class="w-full flex items-center justify-between mt-4">
            <div class="relative w-1/3">
                <Button on:click={() => changePageUser(currentPageUser - 1)} disabled={currentPageUser === 1}>Précédent</Button>
            </div>
            <div class="relative w-1/3 flex justify-center">
                <span class="mx-2 text-sm italic">Page {currentPageUser} sur {totalPagesUser}</span>
            </div>
            <div class="relative w-1/3 flex justify-end">
                <Button on:click={() => changePageUser(currentPageUser + 1)} disabled={currentPageUser === totalPagesUser}>Suivant</Button>
            </div>
        </div>
        </Card.Content>
      </Card.Root>


      <Card.Root class="col-span-10 row-span-2">
        <Card.Header class="flex flex-row items-center justify-between space-y-0 ">
          <Card.Title class="text-base font-medium">Recommendations</Card.Title>
        </Card.Header>
        <Card.Content>
          <div class="text-2xl font-bold">+573</div>
          <p class="text-muted-foreground text-xs">+201 since last hour</p>
        </Card.Content>
      </Card.Root>
    </div>
</main>


<style>

</style>