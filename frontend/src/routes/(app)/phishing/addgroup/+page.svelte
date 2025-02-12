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
    import type { User, UserPagination } from '$types/users';

    let group_name= "";
    let loading_data = true;
    let currentPage = 0;
    const rowsPerPageUser = 4;
    let data: UserPagination = {users: [],page: {size: 0,totalElements: 0,totalPages: 0,number: 0}};

    let selectedUsers : User[] = [];

    onMount(async () => {
      try {
        data = await fetch(`/api/db/users?size=${rowsPerPageUser}&page=${currentPage}`).then(res => res.json());
      } catch (error) {
        console.error("Erreur lors de la récupération du groupe:", error);
      } finally {
        loading_data = false;
      }
    });

    async function getUsers() {
        try {
        data = await fetch(`/api/db/users?size=${rowsPerPageUser}&page=${currentPage}`).then(res => res.json());
        console.log(data.page.totalElements);
        } catch(e) {
        console.error('Error while fetching users: ', e);
        }
    }

    async function nextPage(){
        currentPage++;
        await getUsers();
    }

    async function prevPage(){
        currentPage--;
        await getUsers();
    }


    function isUserSelected(user: User) {
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
        await fetch(`/api/phishing/groups`, {
            method: 'POST',
            body: JSON.stringify(groupJson),
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
            <AlertDialog.Title class="text-left">Créer un groupe</AlertDialog.Title>
        </AlertDialog.Header>
        <div class="flex flex-col gap-6">
            <Separator width={'w-full'} height={'h-px'}/>
            <div class="nom flex flex-col gap-3">
                <Label for="name">Nom du groupe</Label>
                <Input id="name" type="text" bind:value={group_name} />
            </div>
            <div class="users flex flex-col">
                {#if loading_data}
                    <div class="flex justify-center items-center h-full">
                        <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
                    </div>
                {:else}
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
                        {#each data.users as user}
                        <Table.Row>
                            <Table.Cell>{user.lastName}</Table.Cell>
                            <Table.Cell>{user.firstName}</Table.Cell>
                            <Table.Cell>{user.email}</Table.Cell>
                            <Table.Cell>{user.position}</Table.Cell>
                            <Table.Cell>
                                <Checkbox 
                                    checked={isUserSelected(user)}
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
                            <Button class="bg-accent" on:click={prevPage} disabled={currentPage === 0}>Précédent</Button>
                          </div>
                          <div class="relative w-1/3 flex justify-center">
                            <span class="mx-2 text-sm italic">Page {currentPage+1} sur {data.page.totalPages}</span>
                          </div>
                          <div class="relative w-1/3 flex justify-end">
                            <Button class="bg-accent" on:click={nextPage} disabled={currentPage+1 === data.page.totalPages}>Suivant</Button>
                          </div>
                    </div>
                    <Separator width={'w-full'} margin_top={'mt-6'} margin_bottom={'mb-3'} height={'h-px'}/>
                </div>
                {/if}
            </div>
        </div>
        <AlertDialog.Footer>
            <AlertDialog.Cancel on:click={closeAlertDialog}>Annuler</AlertDialog.Cancel>
            <ConfirmPopup description="Création du groupe" name="Enregistrer" style="bg-accent" functionToCall={createGroup}/>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
{/if}