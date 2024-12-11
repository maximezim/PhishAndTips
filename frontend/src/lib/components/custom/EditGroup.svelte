<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import * as Table from "$lib/components/ui/table";
    import { Badge } from "$lib/components/ui/badge";
    import { Input } from "$lib/components/ui/input";
    import { Label } from "$lib/components/ui/label";
	import Button from "../ui/button/button.svelte";
	import { Trigger } from "../ui/dropdown-menu";
  
    type Group = {
        id: number;
        name: string;
        date: string;
        status: string;
        nb: number;
    };
  
    export let group: Group;
  
    let showAlertDialog = false;
  
    function openAlertDialog() {
        console.log(group.id);
        showAlertDialog = true;
    }
  
    function closeAlertDialog() {
        showAlertDialog = false;
    }
  </script>
  
  

<AlertDialog.Root open={showAlertDialog} on:close={closeAlertDialog}>
    <AlertDialog.Trigger asChild let:builder>
        <Table.Row class="cursor-pointer" on:click={openAlertDialog}>
            <Table.Cell>
                <div class="font-medium">{group.name}</div>
                <div class="text-muted-foreground hidden text-sm md:inline">{group.date}</div>
            </Table.Cell>
            <Table.Cell class="hidden sm:table-cell">
                <Badge class="text-xs" variant="secondary">{group.status}</Badge>
            </Table.Cell>
            <Table.Cell class="text-right">
                <Button variant='outline' builders={[builder]}>Modifier</Button>
            </Table.Cell>
            </Table.Row>
        </AlertDialog.Trigger>
    <AlertDialog.Content class="max-w-4xl flex flex-col">
        <AlertDialog.Header>
            <AlertDialog.Title>Modifier le groupe</AlertDialog.Title>
            <AlertDialog.Description>
                Modifiez les informations nécessaires pour ce groupe.
            </AlertDialog.Description>
        </AlertDialog.Header>
        <div class="flex flex-col">
            <div class="nom flex flex-col gap-3">
                <Label for="name">Nom du groupe</Label>
                <Input id="name" type="text" bind:value={group.name} />
            </div>
        </div>
        <AlertDialog.Footer>
            <AlertDialog.Cancel on:click={closeAlertDialog}>Annuler</AlertDialog.Cancel>
            <AlertDialog.Action>Enregistrer</AlertDialog.Action>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>
