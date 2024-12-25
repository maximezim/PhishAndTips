<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import { Button } from "$lib/components/ui/button";
    export let name = "";
    export let type = "";
    export let description = "";
    export let style = "";
    export let functionToCall = () => {};

    function getButtonVariant() {
        switch (type) {
            case "destructive":
                return "destructive";
            case "secondary":
                return "secondary";
            default:
                return "default";
        }
    }
</script>

<AlertDialog.Root>
    <AlertDialog.Trigger asChild let:builder>
        <Button class="{style}" variant={getButtonVariant()} builders={[builder]}>{name}</Button>
    </AlertDialog.Trigger>
    <AlertDialog.Content class="max-w-5xl flex flex-col">
        <AlertDialog.Header>
            <AlertDialog.Title>{description}</AlertDialog.Title>
            <AlertDialog.Description>Etes-vous sûr ? La {description.toLocaleLowerCase()} sera définitive.</AlertDialog.Description>
        </AlertDialog.Header>
        <AlertDialog.Footer>
            <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
            <AlertDialog.Action on:click={functionToCall} class="bg-accent">Confirmer</AlertDialog.Action>
        </AlertDialog.Footer>
    </AlertDialog.Content>
</AlertDialog.Root>