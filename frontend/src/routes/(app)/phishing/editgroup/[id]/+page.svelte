<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import * as Table from "$lib/components/ui/table";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
    import Checkbox from "$lib/components/ui/checkbox/checkbox.svelte";
	import Button from "$lib/components/ui/button/button.svelte";
	import { onMount } from "svelte";
	import Separator from "$lib/components/custom/Separator.svelte";
    import { goto } from "$app/navigation";
    import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
	import { page } from "$app/stores";
    import { get } from "svelte/store";
    import type { User } from '$types/users';

    interface Group {
        id: string;
        name: string;
        modified_date: string;
        targets: User[];
    }

    let group : Group;
    let users : User[] = [];
    let usersFromDb : User[] = [];
    let loading_data = true;
    let selectedUsers : User[] = [];

    let currentPageUser = 1;
    const rowsPerPageUser = 4;
    let totalPagesUser = 1;

    onMount(async () => {
      try {
        let id = "";
        const data = get(page).data;
        if (data && data.id) {
            id = data.id;
        } else {
            console.error('ID is undefined');
        }
        const groupResponse = await fetch(`/api/phishing/groups/details?id=${encodeURIComponent(id)}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        group = await groupResponse.json();
        usersFromDb = await fetch("/api/db/users").then(res => res.json());
      } catch (error) {
        console.error("Erreur lors de la récupération du groupe:", error);
      } finally {
        console.log(group);
        users = group.targets;
        let nbuser = usersFromDb.length;
        totalPagesUser = Math.ceil(nbuser / rowsPerPageUser);
        selectedUsers = usersFromDb.filter(user => users.some(User => User.email === user.email));
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

    function isUserSelected(user: User) {
        return selectedUsers.some(selectedUser => selectedUser.email === user.email);
    }
  
    function closeAlertDialog() {
        goto("/phishing");
    }

    function userToGophishUser(){
        return selectedUsers.map(user => {
            return {
                first_name: user.firstName,
                last_name: user.lastName,
                email: user.email,
                position: user.position
            }
        });
    }

    async function saveAndClose() {
        const groupID = Number(group.id);
        const modifiedDate = new Date().toISOString();
        const groupJson = {
            id: groupID,
            name: group.name,
            modified_date: modifiedDate,
            targets: selectedUsers
        };
        await fetch(`/api/phishing/groups`, {
            method: 'PUT',
            body: JSON.stringify(groupJson),
            headers: {
                'Content-Type': 'application/json'
            }
        });
        closeAlertDialog();
    }

    async function deleteGroup() {
        const groupID = Number(group.id);
        await fetch(`/api/phishing/groups`, {
            method: 'DELETE',
            body: JSON.stringify(groupID),
            headers: {
                'Content-Type': 'application/json'
            }
        });
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
            <AlertDialog.Title class="text-left">Modifier le groupe</AlertDialog.Title>
        </AlertDialog.Header>
        <div class="flex flex-col gap-6">
            <Separator width={'w-full'}/>
            <div class="nom flex flex-col gap-3">
                <Label for="name">Nom du groupe</Label>
                <Input id="name" type="text" bind:value={group.name} />
            </div>
            <div class="users flex flex-col">
                <Label for="users">Utilisateurs</Label>
                <div class="table-container h-64 mt-3">
                <Table.Root class="bg-accent/[0.03] ">
                    <Table.Header>
                        <Table.Row>
                            <Table.Head>Nom</Table.Head>
                            <Table.Head>Prénom</Table.Head>
                            <Table.Head class="hidden sm:table-cell">Email</Table.Head>
                            <Table.Head class="hidden sm:table-cell">Position</Table.Head>
                            <Table.Head>Inclus</Table.Head>
                      </Table.Row>
                    </Table.Header>
                    <Table.Body>
                        {#each getCurrentPageRowsUser() as user}
                        <Table.Row>
                            <Table.Cell>{user.lastName}</Table.Cell>
                            <Table.Cell>{user.firstName}</Table.Cell>
                            <Table.Cell class="hidden sm:table-cell">{user.email}</Table.Cell>
                            <Table.Cell class="hidden sm:table-cell">{user.position}</Table.Cell>
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
        <AlertDialog.Footer class="flex flex-row justify-end w-full">
            <ConfirmPopup style="mr-auto" type="destructive" description="Suppression du groupe" name="Supprimer" functionToCall={deleteGroup}/>
            <div class="flex space-x-2">
                <AlertDialog.Cancel class="mt-0" on:click={closeAlertDialog}>Annuler</AlertDialog.Cancel>
                <ConfirmPopup name="Sauvegarder" description="Modification du groupe" style="bg-accent" functionToCall={saveAndClose}/>
            </div>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
{/if}