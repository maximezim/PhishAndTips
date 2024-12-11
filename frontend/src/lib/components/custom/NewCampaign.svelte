<script lang="ts">
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import * as Select from "$lib/components/ui/select";
    import { Input } from "../ui/input";
	  import { Button } from "../ui/button";
    import DataPicker from "./DataPicker.svelte";

    type Group = {
        name: string;
        date: string;
        status: string;
        nb: number;
    };

    type Mail = {
        name: string;
    };

    type Template = {
        name: string;
    };
        
    export let groups: Group[] = [];
    export let mails: Mail[] = [];
    export let templates: Template[] = [];

    let selectedMail: Mail | undefined = undefined;
    let selectedTemplate: string = '';

    function createCampaign(){
    }
  </script>
  
    <AlertDialog.Root>
        <AlertDialog.Trigger asChild let:builder>
            <Button class="bg-accent" builders={[builder]}>Créer une campagne</Button>
          </AlertDialog.Trigger>
      <AlertDialog.Content class="max-w-4xl flex flex-col">
        <AlertDialog.Header>
          <AlertDialog.Title>Créer une nouvelle campagne</AlertDialog.Title>
          <AlertDialog.Description>
            Remplissez les informations nécessaires pour créer une nouvelle campagne de phishing.
          </AlertDialog.Description>
          

          <div class="grid grid-cols-2 w-full gap-x-8 gap-y-4 pt-5">
            <div class="name flex flex-col gap-2">
                <p class="text-sm">Choisir un nom</p>
                <Input type="email" placeholder="Nom de la campagne" class="w-full" />
            </div>
            <div class="group flex flex-col gap-2">
                <p class="text-sm">Choisir un groupe</p>
                <Select.Root>
                    <Select.Trigger>
                      <Select.Value placeholder="Groupe" />
                    </Select.Trigger>
                    <Select.Content>
                      {#each groups as group}
                        <Select.Item value={group.name}>{group.name}</Select.Item>
                      {/each}
                    </Select.Content>
                  </Select.Root>
            </div>
            <div class="mail flex flex-col gap-2">
                <p class="text-sm">Choisir un mail</p>
                <Select.Root>
                    <Select.Trigger>
                      <Select.Value placeholder="Mail" />
                    </Select.Trigger>
                    <Select.Content>
                      {#each mails as mail}
                        <Select.Item value={mail.name}>{mail.name}</Select.Item>
                      {/each}
                    </Select.Content>
                  </Select.Root>
            </div>
            <div class="template flex flex-col gap-2">
                <p class="text-sm">Choisir un template</p>
                <Select.Root>
                    <Select.Trigger>
                      <Select.Value placeholder="Template" />
                    </Select.Trigger>
                    <Select.Content>
                      {#each templates as template}
                        <Select.Item value={template.name}>{template.name}</Select.Item>
                      {/each}
                    </Select.Content>
                  </Select.Root>
            </div>

            <div class="relative preview_mail h-48">
                <span class="flex w-full h-full rounded bg-accent/[0.1] justify-center items-center">{selectedMail}</span>
            </div>
            <div class="relative preview_template h-48">
                <span class="flex w-full h-full rounded bg-accent/[0.1] justify-center items-center">{selectedTemplate}</span>
            </div>


            <div class="date flex flex-col gap-2">
                <p class="text-sm">Choisir une date de fin</p>
                <DataPicker />
            </div>
            
            

          </div>
        </AlertDialog.Header>
        <AlertDialog.Footer>
          <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
          <AlertDialog.Action on:click={createCampaign}>Créer</AlertDialog.Action>
        </AlertDialog.Footer>
      </AlertDialog.Content>
    </AlertDialog.Root>
