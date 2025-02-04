<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import * as Table from "$lib/components/ui/table";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import Checkbox from "$lib/components/ui/checkbox/checkbox.svelte";
	import Button from "$lib/components/ui/button/button.svelte";
	import { onMount } from "svelte";
	import Separator from "$lib/components/custom/Separator.svelte";
    import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
    import { goto } from "$app/navigation";
	import PhishingService from "$lib/services/PhishingService";

    interface Target {
        email: string;
        first_name: string;
        last_name: string;
        position: string;
    }

    let group_name= "";
    let loading_data = true;
    let currentPageUser = 1;
    const rowsPerPageUser = 4;
    let totalPagesUser = 1;

    let selectedUsers : Target[] = [];
    let usersFromDb : Target[] = [];

    onMount(async () => {
        try {

        } catch (error) {
            console.error("Erreur lors de la récupération du groupe:", error);
        } finally {
            usersFromDb = [
                {email: "test@mail.com", first_name: "John", last_name: "Doe", position: "CEO"},
                {email: "test2@mail.com", first_name: "Jane", last_name: "Doe", position: "CFO"},
                {email: "test3@mail.com", first_name: "John", last_name: "Smith", position: "CTO"},
                {email: "jsmith@insa.com", first_name: "John", last_name: "Smith", position: "manager"},
                {email: "llita@insa.fr", first_name: "Lea", last_name: "lita", position: "rh"},
                {email: "mpalvin@insa.fr", first_name: "Mael", last_name: "Palvin", position: "rh"}
            ]
            totalPagesUser = Math.ceil(usersFromDb.length / rowsPerPageUser);
            loading_data = false;
        }
    });


    function changePageUser(page: number) {
        if (page >= 1 && page <= totalPagesUser) {
            currentPageUser = page;
        }
    }
    function getCurrentPageRowsUser() {
        const start = (currentPageUser - 1) * rowsPerPageUser;
        const end = start + rowsPerPageUser;
        return usersFromDb.slice(start, end);
    }

    function isUserSelected(user: Target) {
        return selectedUsers.some(selectedUser => selectedUser.email === user.email);
    }

    function closeAlertDialog() {
        goto("/phishing");
    }

    async function createGroup(){
        const groupJson = {
            name: group_name,
            targets: selectedUsers,
        };
        console.log(groupJson);
        // PhishingService.createGroup(groupJson);

        const response = await fetch('/api/groups/create', {
			method: 'POST',
			body: JSON.stringify(groupJson),
			headers: {
				'Content-Type': 'application/json'
			}
		});

		const response_json = await response.json();

        console.log("page.svelte : ", response_json);

        closeAlertDialog();
    }

</script>

{#if loading_data}
    <div class="flex justify-center items-center h-full">
        <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
    </div>
{:else}
<AlertDialog.Root open={!loading_data} on:close={closeAlertDialog}>
    <AlertDialog.Content class="max-w-4xl flex flex-col">
        <AlertDialog.Header>
            <AlertDialog.Title>Créer un groupe</AlertDialog.Title>
        </AlertDialog.Header>
        <div class="flex flex-col gap-6">
            <div class="nom flex flex-col gap-3">
                <Label for="name">Nom du groupe</Label>
                <Input id="name" type="text" bind:value={group_name} />
            </div>
            <div class="users flex flex-col">
                <Label for="users">Utilisateurs</Label>
                <div class="table-container h-64 mt-3">
                <Table.Root class="bg-accent/[0.03] ">
                    <Table.Header>
                        <Table.Row>
                            <Table.Head>Nom</Table.Head>
                            <Table.Head>Prénom</Table.Head>
                            <Table.Head>Email</Table.Head>
                            <Table.Head>Position</Table.Head>
                            <Table.Head>Inclus</Table.Head>
                        </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {#each getCurrentPageRowsUser() as user}
                        <Table.Row>
                            <Table.Cell>{user.last_name}</Table.Cell>
                            <Table.Cell>{user.first_name}</Table.Cell>
                            <Table.Cell>{user.email}</Table.Cell>
                            <Table.Cell>{user.position}</Table.Cell>
                            <Table.Cell>
                                <Checkbox 
                                    checked={selectedUsers.includes(user)}
                                    on:click={() => {
                                        if (isUserSelected(user)) {
                                            selectedUsers = selectedUsers.filter(selectedUser => selectedUser.email !== user.email);
                                        } else {
                                            selectedUsers = [...selectedUsers, user];
                                        }
                                    }}
                                />
                            </Table.Cell>
                        </Table.Row>
                        {/each}
                    </Table.Body>
                </Table.Root>
                </div>
                <div class="footer mt-2">
                    <div class="w-full flex items-center justify-between mt-4">
                        <div class="relative w-1/3">
                            <Button class="bg-accent" on:click={() => changePageUser(currentPageUser - 1)} disabled={currentPageUser=== 1}>Précédent</Button>
                        </div>
                        <div class="relative w-1/3 flex justify-center">
                            <span class="mx-2 text-sm italic">Page {currentPageUser} sur {totalPagesUser}</span>
                        </div>
                        <div class="relative w-1/3 flex justify-end">
                            <Button class="bg-accent" on:click={() => changePageUser(currentPageUser + 1)} disabled={currentPageUser === totalPagesUser}>Suivant</Button>
                        </div>
                    </div>
                    <Separator width={'w-full'} margin_top={'mt-6'} margin_bottom={'mb-3'} height={'h-px'}/>
                </div>
            </div>
        </div>
        <AlertDialog.Footer>
            <AlertDialog.Cancel on:click={closeAlertDialog}>Annuler</AlertDialog.Cancel>
            <ConfirmPopup description="Création du groupe" name="Enregistrer" style="bg-accent" functionToCall={createGroup}/>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
{/if}
