<script lang="ts">
  import { onMount } from 'svelte';
  import { Button } from '$lib/components/ui/button';
  import * as Card from '$lib/components/ui/card';
  import * as Table from '$lib/components/ui/table';
  import type { User, UserPagination } from '$types/users';
  import 'iconify-icon';
  import Separator from '$lib/components/custom/Separator.svelte';
	import UserPopup from './UserPopup.svelte';
	import UserEditPopup from './UserEditPopup.svelte';
	import ConfirmPopup from '../ConfirmPopup.svelte';
	import UserAddPopup from './UserAddPopup.svelte';
	import UserImportPopup from './UserImportPopup.svelte';

  let currentPage = 0;
  const rowsPerPageUser = 10;
  let data: UserPagination = {users: [],page: {size: 0,totalElements: 0,totalPages: 0,number: 0}};

  onMount(async () => {
    await getUsers();
  });

  async function getUsers() {
    try {
      data = await fetch(`/api/db/users?size=${rowsPerPageUser}&page=${currentPage}`).then(res => res.json());
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

  async function deleteUser(user: User) {
    try {
      await fetch('/api/db/user', {
			method: 'DELETE',
			body: JSON.stringify(user.email),
			headers: {
				'Content-Type': 'application/json'
			}
		});
      sessionStorage.setItem("showSuccessToast", "Utilisateur supprimé avec succès");
    } catch(e) {
      console.error('Error while fetching users: ', e);
      sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors de la suppression de l'utilisateur");
    } finally {
      closeAlertDialog();
    }
  }

  function closeAlertDialog() {
    window.location.reload();
  }


</script>

<Card.Root class="col-span-10 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Utilisateurs</span>
      <div class="flex flex-row gap-2 align-middle">
        <UserAddPopup />
        <UserImportPopup />
        <iconify-icon class="text-3xl text-accent flex flex-col align-middle" icon="mingcute:group-3-fill"></iconify-icon>
      </div>
      
    </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6">
    <Table.Root class="bg-accent/[0.03]">
      <Table.Header>
        <Table.Row>
          <Table.Head>Nom</Table.Head>
          <Table.Head>Prénom</Table.Head>
          <Table.Head>Email</Table.Head>
          <Table.Head>Position</Table.Head>
          <Table.Head class="text-right">Actions</Table.Head>
        </Table.Row>
      </Table.Header>

      <Table.Body>
        {#each data.users as user}
          <Table.Row>
            <Table.Cell>{user.lastName}</Table.Cell>
            <Table.Cell>{user.firstName}</Table.Cell>
            <Table.Cell>{user.email}</Table.Cell>
            <Table.Cell>{user.position}</Table.Cell>
            <Table.Cell class="flex flex-row align-middle justify-end gap-2"><UserPopup user={user} /><UserEditPopup user={user} /><ConfirmPopup description="Suppression de l'utilisateur" icon="mingcute:delete-2-fill" style="bg-red-500 hover:bg-red-700 text-xl text-white py-0 px-3" functionToCall={() => deleteUser(user)} /></Table.Cell>
          </Table.Row>
        {/each}
      </Table.Body>
    </Table.Root>
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
  </Card.Content>
</Card.Root>
