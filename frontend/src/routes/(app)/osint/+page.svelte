<script lang='ts'>
	  import Button from "$lib/components/ui/button/button.svelte";
    import 'iconify-icon';
    import * as Tabs from "$lib/components/ui/tabs";
    import * as Table from "$lib/components/ui/table";
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import { onMount } from 'svelte';
	import { run } from "svelte/legacy";

    interface ParsedResult {
      Data: string;
      Type: string;
    }

    interface ParsedData {
      count: number;
      results: ParsedResult[];
    }

    interface ExportResult {
      data: string;
      module: string;
      last_seen: string;
      scan_name: string;
      event_type: string;
      scan_target: string;
      source_data: string;
      false_positive: number;
    }

    interface ScanResult {
      parsed_data: ParsedData;
      export_result: ExportResult[];
    }

    interface MyScan {
      target: string;
      id: string;
      modules: string;
      status: string;
      result: string; 
      spiderfootScanId: string;
      createdAt: string;
      updatedAt: string;
    }

    interface Target {
      first_name: string;
      last_name: string;
      email: string;
      position: string;
    }
    
    interface User {
      target: Target;
      scan: MyScan | null;
    }

    let myScan: MyScan | [];
    let scanResult: ScanResult | null = null;
    let isAdmin: boolean = false;
    let usersFromDb : Target[] = [];
    let users: User[] = [];

    let loading = true;	
    let canScan = true;
    let running = false;
    let dateScan = "";
    let status = "";
    let nbResults = "";

    let currentPageUser = 1;
    const rowsPerPageUser = 10;
    let totalPagesUser = 1;

    onMount(async () => {
      try {
        isAdmin = await fetch("/api/auth/admin").then(res => res.json());
        myScan = await fetch("/api/osint/user").then(res => res.json());
        if(isAdmin){
          usersFromDb = await fetch("/api/db/users").then(res => res.json());
          users = await Promise.all(usersFromDb.map(async (user: Target) => {
            const groupJson = {
              email : user.email
            };
            let scan = await fetch(`/api/osint/admin/get-scan`, {
                method: 'POST',
                body: JSON.stringify(groupJson),
                headers: {
                  'Content-Type': 'application/json'
                }
            }).then(res => res.json());
            return {
              target: user,
              scan: scan
            };
          }));
        }
      } catch (error) {
        console.error("Erreur : ", error);
      } finally {
        console.log(usersFromDb);
        if(myScan && !Array.isArray(myScan)){
          scanResult = JSON.parse(myScan.result);
          const dateStr = myScan.updatedAt;
          canScan = checkDate(dateStr);
          const date = new Date(dateStr);
          const options: Intl.DateTimeFormatOptions = { 
            day: '2-digit', 
            month: 'long', 
            year: 'numeric', 
            hour: '2-digit', 
            minute: '2-digit',
            hour12: false
          };
          switch(myScan.status){
            case "completed":
              status = "Terminé";
              dateScan = "Fin du scan : "+date.toLocaleDateString("fr-FR", options);
              if (scanResult && scanResult.parsed_data.results.length > 0) {
                nbResults = " - " + scanResult.parsed_data.results.length + " résultat(s) exporté(s)";
              }
              break;
            case "running":
              status = "En cours";
              dateScan = "Début du scan : "+date.toLocaleDateString("fr-FR", options);
              running = true;
              break;
            case "error":
              status = "Erreur";
              break;
            default:
              status = "Inconnu";
              break;
          }
          if(isAdmin){
            totalPagesUser = Math.ceil(users.length / rowsPerPageUser);
          }
        }else{
          status = "Inexistant";
          dateScan = "Aucun scan effectué";
        }
        loading = false;
      }
    });

    function checkDate(dateStr: string) {
      const dateScan = new Date(dateStr);
      const now = new Date();
      const diffInMs = now.getTime() - dateScan.getTime();
      const diffInHours = diffInMs / (1000 * 60 * 60);
      return diffInHours > 24;
    }

    function formatDate(dateString: string): string {
      const date = new Date(dateString);
      const options: Intl.DateTimeFormatOptions = { 
        day: '2-digit', 
        month: 'long', 
        year: 'numeric', 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: false
      };
      return date.toLocaleDateString("fr-FR", options);
    }

    async function searchOsint() {
      if(running){
        location.reload();
      }
      try{
        await fetch(`/api/osint/user`, {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json'
            }
        });
      }catch(e){
        console.log(e);
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
    function formatHtmlContent(content: string) {
      return content.replace(/\\n/g, '\n').replace(/\\t/g, '\t');
    }
    function parseScanResult(scanResult: string) {
      try {
        return JSON.parse(scanResult);
      } catch (error) {
        console.error('Error parsing scan result:', error);
        return null;
      }
    }

  </script>

  <div class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-2 md:p-8">

    {#if isAdmin}
    <Tabs.Root value="user" class="w-full">
      <Tabs.List class="grid max-w-sm grid-cols-2">
        <Tabs.Trigger value="user">Perso</Tabs.Trigger>
        <Tabs.Trigger value="admin">Admin</Tabs.Trigger>
      </Tabs.List>
      <Tabs.Content value="user" class="w-full" >
        <div class="w-full p-6 light-bg flex items-center justify-between">
          <div class="flex flex-col gap-2">
            <p class="text-medium font-semibold">Status : {status} {nbResults}</p>
            <p class="text-sm italic text-gray-600">{dateScan}</p>
          </div>
          <Button class="bg-accent px-12 py-6 relative z-10"  on:click={searchOsint} disabled={!canScan && !running}>
            {#if running}
              <span class="text-base">Actualiser</span>
            {:else}
              {#if canScan}
                <span class="text-base">Nouveau scan</span>
              {:else}
                <span class="text-base">Scan disponible demain</span>
              {/if}
            {/if}
          </Button>
        </div>
    
        {#if status === "Terminé"}
        <div class="results grid grid-cols-2">
            {#if scanResult}
              {#each scanResult.parsed_data.results as result, index}
                <div class="result flex flex-col gap-2 p-4 bg-white border-2 border-gray-100 overflow-auto">
                  <p class="text-medium font-semibold">{result.Type}</p>
                  <p class="text-sm italic text-gray-600">{result.Data}</p>
                </div>
              {/each}
            {/if}
        </div>

        <div class="analyse w-full p-6 light-bg flex items-center mt-6">
          <div class="flex flex-col gap-2">
            <p class="text-medium font-semibold">Analyse des résultats</p>
          </div>
        </div>
        <div class="bg-white w-full p-6 border-2 border-t-0 border-gray-100">
         
        </div>
        {/if}

      </Tabs.Content>
      <Tabs.Content value="admin">
        <div class="w-full shadow">
        <Table.Root class="w-full">
          <Table.Header class="bg-violet-50">
              <Table.Row>
                  <Table.Head>Nom</Table.Head>
                  <Table.Head>Prénom</Table.Head>
                  <Table.Head>Email</Table.Head>
                  <Table.Head>Position</Table.Head>
                  <Table.Head>Date scan</Table.Head>
                  <Table.Head>Scan</Table.Head>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {#if loading}
              <div class="flex justify-center items-center h-full">
                <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
              </div>
            {:else}
            {#each getCurrentPageRowsUser() as user}
              <Table.Row class="hover:bg-gray-50 bg-white">
                  <Table.Cell>{user.target.last_name}</Table.Cell>
                  <Table.Cell>{user.target.first_name}</Table.Cell>
                  <Table.Cell>{user.target.email}</Table.Cell>
                  <Table.Cell>{user.target.position}</Table.Cell>
                  <Table.Cell>{user.scan && Array.isArray(user.scan) && user.scan.length === 0 ? "-" : user.scan ? formatDate(user.scan.updatedAt) : "-"}</Table.Cell>
                  <Table.Cell>
                    {#if user.scan && Array.isArray(user.scan) && user.scan.length === 0}
                      -
                    {:else}
                    <AlertDialog.Root>
                      <AlertDialog.Trigger>
                        <iconify-icon class="text-2xl" style="color: #9183ec" icon="mingcute:eye-2-fill"></iconify-icon>
                      </AlertDialog.Trigger>
                      <AlertDialog.Content class="max-w-3xl">
                        {#if user.scan}
                          <AlertDialog.Header class="flex flex-row justify-between items-center">
                            <AlertDialog.Title>Scan du {formatDate(user.scan.updatedAt)}</AlertDialog.Title>
                            <Button class="bg-accent" on:click={() => {}}>Nouveau scan</Button>
                          </AlertDialog.Header>
                          <div class="h-[60vh] overflow-y-auto p-1">
                            {#each parseScanResult(user.scan.result).parsed_data.results as result, index}
                              <div class="result flex flex-col mt-2 gap-2 p-4 bg-gray-50 shadow">
                                <p class="text-medium font-semibold">{result.Type}</p>
                                <p class="text-sm italic text-gray-600">{result.Data}</p>
                              </div>
                            {/each}
                            <div class="w-full mt-2 p-5 bg-accent/[0.07] shadow rounded h-[60svh] overflow-y-auto">
                                <iframe srcdoc={formatHtmlContent(user.scan.result)} class="w-full h-full rounded-shadow" title="Redirection Model"></iframe>
                            </div>
                          </div>
                        {/if}
                        <AlertDialog.Footer>
                          <AlertDialog.Action>Fermer</AlertDialog.Action>
                        </AlertDialog.Footer>
                      </AlertDialog.Content>
                    </AlertDialog.Root>
                    {/if}
                 </Table.Cell>
              </Table.Row>
              {/each}
            {/if}
          </Table.Body>
        </Table.Root>
        <div class="w-full bg-gray-50 p-3 border-t-2 border-gray-100">
          <div class="w-full flex items-center justify-between">
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
        </div>
      </div>
      </Tabs.Content>
    </Tabs.Root>
    {:else}

    <div class="w-full p-6 light-bg flex items-center justify-between ">
      <div class="flex flex-col gap-2">
        <p class="text-medium font-semibold">Status : {status} {nbResults}</p>
        <p class="text-sm italic text-gray-600">{dateScan}</p>
      </div>
      <Button class="bg-accent px-12 py-6 relative z-10"  on:click={searchOsint} disabled={!canScan && !running}>
        {#if running}
          <span class="text-base">Actualiser</span>
        {:else}
          {#if canScan}
            <span class="text-base">Nouveau scan</span>
          {:else}
            <span class="text-base">Scan disponible demain</span>
          {/if}
        {/if}
      </Button>
    </div>

    <div class="results grid grid-cols-2 gap-2 bg-white">
      {#if scanResult}
        {#each scanResult.parsed_data.results as result, index}
          <div class="result flex flex-col gap-2 p-4 bg-white shadow overflow-auto">
            <p class="text-medium font-semibold">{result.Type}</p>
            <p class="text-sm italic text-gray-600">{result.Data}</p>
          </div>
        {/each}
      {/if}
    </div>

    {#if status === "Terminé"}
    <div class="analyse w-full p-6 light-bg flex items-center mt-6">
      <div class="flex flex-col gap-2">
        <p class="text-medium font-semibold">Analyse des résultats</p>
      </div>
    </div>
    <div class="bg-white w-full p-6 border-2 border-t-0 border-gray-100"></div>
    {/if}
    
    {/if}
  </div>

  


<style>
  .light-bg{
    background-color: #f4f2fd;
  }
</style>
   