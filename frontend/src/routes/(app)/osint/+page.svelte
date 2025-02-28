<script lang='ts'>
	  import Button from "$lib/components/ui/button/button.svelte";
    import 'iconify-icon';
    import * as Tabs from "$lib/components/ui/tabs";
    import * as Table from "$lib/components/ui/table";
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import type { User, UserPagination } from '$types/users';
    import type { ParsedResult, ParsedData, ExportResult, ScanResult, MyScan, UserScan } from '$types/osint';
    import { typeTranslations } from '$types/osint';
    import { onMount } from 'svelte';
    import { showSuccessToast, showErrorToast } from "$lib/toast";

    let myScan: MyScan | [];
    let scanResult: ScanResult | null = null;
    let isAdmin: boolean = false;
    let usersFromDb : User[] = [];
    let users: UserScan[] = [];

    let loading = true;	
    let canScan = true;
    let running = false;
    let dateScan = "";
    let status = "";
    let nbResults = "";
    let myScore = 0;
    let myScoreColor = "";
    let groupedResults = new Map<string, ParsedResult[]>();

    let currentPage = 0;
    const rowsPerPageUser = 10;
    let data: UserPagination = {users: [],page: {size: 0,totalElements: 0,totalPages: 0,number: 0}};

    onMount(async () => {
      try {
        isAdmin = await fetch("/api/can-access/can-get-all-users").then(res => res.json());
        myScan = await fetch("/api/osint/user").then(res => res.json());
        myScore = await fetch("/api/scoring/osint").then(res => res.json());
        if(isAdmin){
          getUsers().then(async()=>{
            users = await Promise.all(data.users.map(async (user: User) => {
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

              let userScore = await fetch(`/api/scoring/admin/osint`, {
                  method: 'POST',
                  body: JSON.stringify(groupJson),
                  headers: {
                    'Content-Type': 'application/json'
                  }
              }).then(res => res.json());
              let userScoreColor = getScoreColor(userScore);

              
              let groupedResults = null;
              if(scan && !Array.isArray(scan) && scan.status && scan.status === "completed"){
                groupedResults = groupResultsByType(JSON.parse(scan.result).parsed_data.results);
              }
              return {
                target: user,
                scan: scan,
                groupedResults: groupedResults,
                score: userScore,
                scoreColor: "rgb(" + userScoreColor.r + ", " + userScoreColor.g + ", " + userScoreColor.b + ")",
              };
            }));
          });
        }
      } catch (error) {
        console.error("Erreur : ", error);
      } finally {
        let color = getScoreColor(myScore);
        myScoreColor = "rgb(" + color.r + ", " + color.g + ", " + color.b + ")";
        if(myScan && !Array.isArray(myScan) && myScan.result){
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
                groupedResults = groupResultsByType(scanResult.parsed_data.results);
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
        }else{
          status = "Inexistant";
          dateScan = "Aucun scan effectué";
        }
        loading = false;
      }
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
        sessionStorage.setItem("showSuccessToast", "Le scan a été lancé avec succès");
      }catch(e){
        console.error(e);
        sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors du lancement du scan");
      } finally {
        location.reload();
      }
    }

    async function searchOsintUser(email: string) {
      try{
        await fetch(`/api/osint/admin/new-scan`, {
            method: 'POST',
            body: JSON.stringify({email}),
            headers: {
              'Content-Type': 'application/json'
            }
        });
        sessionStorage.setItem("showSuccessToast", "Le scan a été lancé avec succès");
      }catch(e){
        console.error(e);
        sessionStorage.setItem("showErrorToast", "Une erreur s'est produite lors du lancement du scan");
      } finally {
        location.reload();
      }
    }

    function formatHtmlContent(content: string) {
      return content.replace(/\\n/g, '\n').replace(/\\t/g, '\t');
    }


    const gradientColors = [
      { r: 100, g: 255, b: 100 },  // Vert
      { r: 255, g: 255, b: 100 }, // Jaune
      { r: 255, g: 100, b: 100 }, // Rouge
    ];
    function interpolateColor(color1: { r: number, g: number, b: number }, color2: { r: number, g: number, b: number }, factor: number) {
      const result = {
        r: Math.round(color1.r + factor * (color2.r - color1.r)),
        g: Math.round(color1.g + factor * (color2.g - color1.g)),
        b: Math.round(color1.b + factor * (color2.b - color1.b))
      };
      return result;
    }
    function getScoreColor(percentage : number) {
      if (percentage <= 0) return gradientColors[0];
      if (percentage >= 10) return gradientColors[gradientColors.length - 1];

      const totalSegments = gradientColors.length - 1;
      const segment = Math.floor((percentage / 10) * totalSegments);
      const segmentPercentage = (percentage / 10) * totalSegments - segment;

      return interpolateColor(gradientColors[segment], gradientColors[segment + 1], segmentPercentage);
    }

    function getTypeTranslation(type: string): string {
      return typeTranslations.get(type) || type;
    }

    function extractSite(data: string): string {
      const match = data.match(/\[(.*?)\]/);
      return match ? match[1] : data;
    }

    function groupResultsByType(results: ParsedResult[]) {
      const groupedResults = new Map<string, ParsedResult[]>();
      results.forEach(result => {
        if (!groupedResults.has(result.Type)) {
          groupedResults.set(result.Type, []);
        }
        const data = result.Data.includes('[') && result.Data.includes(']') ? extractSite(result.Data) : result.Data;
        groupedResults.get(result.Type)?.push({ ...result, Data: data });
      });
      return groupedResults;
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
        <div class="w-full p-6 light-bg flex flex-col sm:flex-row sm:items-center justify-between gap-4">
          <div class="flex flex-col text-left gap-2">
            <p class="text-medium font-semibold">Statut : {status} {nbResults}</p>
            <p class="text-sm italic text-gray-600">{dateScan}</p>
          </div>
          <Button class="w-full sm:w-auto bg-accent sm:px-12 sm:py-6 relative z-10"  on:click={searchOsint} disabled={!canScan && !running}>
            {#if running}
              <span class="sm:text-base">Actualiser</span>
            {:else}
              {#if canScan}
                <span class="sm:text-base">Nouveau scan</span>
              {:else}
                <span class="sm:text-base">Scan disponible demain</span>
              {/if}
            {/if}
          </Button>
        </div>
    
        {#if status === "Terminé"}
        <div class="results grid grid-cols-1 sm:grid-cols-2 gap-1 bg-white mt-1">
          {#each Array.from(groupedResults.entries()) as [type, results]}
            <div class="result-group flex flex-col gap-2 p-4 bg-white shadow overflow-auto">
              <p class="text-medium font-semibold">{getTypeTranslation(type)}</p>
              {#each results as result}
                <p class="text-sm italic text-gray-600">{result.Data}</p>
              {/each}
            </div>
          {/each}
        </div>

        <div class="analyse w-full p-6 light-bg flex items-center mt-6">
          <div class="flex flex-col gap-2">
            <p class="text-medium font-semibold">Analyse des résultats</p>
          </div>
        </div>
        <div class="bg-white w-full p-6 border-2 border-t-0 border-gray-100">
          <div class="relative color-bar h-4 w-full rounded">
            <div class="absolute h-7 w-0.5 top-[50%] translate-y-[-50%] bg-accent" style="left: {myScore*10}%"></div>
          </div>
        </div>
        {/if}

      </Tabs.Content>
      <Tabs.Content value="admin">
        <div class="w-full shadow">
        <Table.Root class="w-full">
          <Table.Header class="bg-violet-50">
              <Table.Row>
                  <Table.Head>Risque</Table.Head>
                  <Table.Head>Nom</Table.Head>
                  <Table.Head>Prénom</Table.Head>
                  <Table.Head class="hidden lg:table-cell">Email</Table.Head>
                  <Table.Head class="hidden lg:table-cell">Position</Table.Head>
                  <Table.Head class="hidden lg:table-cell">Date scan</Table.Head>
                  <Table.Head>Scan</Table.Head>
            </Table.Row>
          </Table.Header>
          <Table.Body>
            {#if loading}
              <div class="flex justify-center items-center h-full">
                <div class="loader ease-linear rounded-full border-8 border-t-8 border-gray-200 h-32 w-32"></div>
              </div>
            {:else}
            {#each users as user}
              <Table.Row class="hover:bg-gray-50 bg-white">
                  <Table.Cell>
                    <span class="flex w-8 h-3 rounded-full border border-accent" style='background-color: {user.scoreColor}'></span>
                  </Table.Cell>
                  <Table.Cell>{user.target.lastName}</Table.Cell>
                  <Table.Cell>{user.target.firstName}</Table.Cell>
                  <Table.Cell class="hidden lg:table-cell">{user.target.email}</Table.Cell>
                  <Table.Cell class="hidden lg:table-cell">{user.target.position}</Table.Cell>
                  <Table.Cell class="hidden lg:table-cell">
                    {#if user.scan && !Array.isArray(user.scan)}
                      {#if user.scan.status == "completed"}
                        {formatDate(user.scan.updatedAt)}
                      {:else if user.scan.status == "running"}
                        En cours
                      {:else}
                        -
                      {/if}
                    {/if}
                  </Table.Cell>
                  <Table.Cell>
                    {#if user.scan && !Array.isArray(user.scan) && user.scan.status == "completed"}
                    <AlertDialog.Root>
                      <AlertDialog.Trigger>
                        <iconify-icon class="text-2xl" style="color: #9183ec" icon="mingcute:eye-2-fill"></iconify-icon>
                      </AlertDialog.Trigger>
                      <AlertDialog.Content class="max-w-3xl p-0 overflow-hidden">
                        <div class="w-full flex justify-between items-center bg-violet-50 p-6">
                          <div class="left flex gap-5">
                            <iconify-icon class="text-7xl p-2 pb-1 bg-white rounded-lg" icon="mingcute:face-fill"></iconify-icon>
                            <div class="flex flex-col justify-center">
                              <p class="text-lg font-semibold">{user.target.firstName} {user.target.lastName}</p>
                              <p class="text-medium">{user.target.email}</p>
                              <p class="text-medium">{user.target.position}</p>
                            </div>
                          </div> 
                          <div class="right">
                            <span class="shadow flex w-10 h-10 rounded-full border-2 border-accent" style='background-color: {user.scoreColor}'></span>
                          </div>
                        </div>
                        {#if user.scan}
                          <AlertDialog.Header class="flex flex-col gap-2 sm:gap-0 sm:flex-row sm:justify-between sm:items-center px-6">
                            <AlertDialog.Title class="text-base sm:text-medium text-left">Scan du {formatDate(user.scan.updatedAt)}</AlertDialog.Title>
                            <Button class="w-full sm:w-auto bg-accent" on:click={()=>{searchOsintUser(user.target.email)}}>Nouveau scan</Button>
                          </AlertDialog.Header>
                          <div class="h-[50vh] overflow-y-auto px-6">
                            {#if user.groupedResults}
                              {#each Array.from(user.groupedResults.entries()) as [type, results]}
                                <div class="result-group flex flex-col mt-2 gap-2 p-4 bg-gray-50 shadow overflow-auto">
                                  <p class="text-medium font-semibold">{getTypeTranslation(type)}</p>
                                  {#each results as result}
                                    <p class="text-sm italic text-gray-600">{result.Data}</p>
                                  {/each}
                                </div>
                              {/each}
                            {/if}
                            <div class="w-full mt-2 p-5 bg-accent/[0.07] shadow rounded h-[60svh] overflow-y-auto">
                                <iframe srcdoc={formatHtmlContent(user.scan.result)} class="w-full h-full rounded-shadow" title="Redirection Model"></iframe>
                            </div>
                          </div>
                        {/if}
                        <AlertDialog.Footer class="p-6 pt-2">
                          <AlertDialog.Action>Fermer</AlertDialog.Action>
                        </AlertDialog.Footer>
                      </AlertDialog.Content>
                    </AlertDialog.Root>
                    {:else}

                    {#if user.scan && !Array.isArray(user.scan) && user.scan.status == "running"}
                      <iconify-icon class="text-2xl" style="color: #f6ad55" icon="mdi:progress-clock"></iconify-icon>
                    {:else}

                    <AlertDialog.Root>
                      <AlertDialog.Trigger>
                        <iconify-icon class="text-2xl" style="color: #9183ec" icon="mingcute:scan-line"></iconify-icon>
                      </AlertDialog.Trigger>
                      <AlertDialog.Content>
                        <AlertDialog.Header>
                          <AlertDialog.Title class="text-base">Lancer un scan</AlertDialog.Title>
                          <AlertDialog.Description class="text-sm">Voulez-vous lancer un scan pour {user.target.firstName} {user.target.lastName} ?</AlertDialog.Description>
                        </AlertDialog.Header>
                        <AlertDialog.Footer>
                          <AlertDialog.Action>Fermer</AlertDialog.Action>
                          <Button class="bg-accent" on:click={()=>{searchOsintUser(user.target.email)}}>Lancer le scan</Button>
                        </AlertDialog.Footer>
                      </AlertDialog.Content>
                    </AlertDialog.Root>
                    {/if}
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
                <Button class="bg-accent" on:click={prevPage} disabled={currentPage === 0}>Précédent</Button>
            </div>
            <div class="relative w-1/3 flex justify-center">
                <span class="mx-2 text-sm italic">Page {currentPage+1} sur {data.page.totalPages}</span>
            </div>
            <div class="relative w-1/3 flex justify-end">
                <Button class="bg-accent" on:click={nextPage} disabled={currentPage+1 === data.page.totalPages}>Suivant</Button>
            </div>
        </div>
        </div>
      </div>
      </Tabs.Content>
    </Tabs.Root>
    {:else}

    <div class="w-full p-6 light-bg flex flex-col sm:flex-row sm:items-center justify-between gap-4">
      <div class="flex flex-col text-left gap-2">
        <p class="text-medium font-semibold">Statut : {status} {nbResults}</p>
        <p class="text-sm italic text-gray-600">{dateScan}</p>
      </div>
      <Button class="w-full sm:w-auto bg-accent sm:px-12 sm:py-6 relative z-10"  on:click={searchOsint} disabled={!canScan && !running}>
        {#if running}
          <span class="sm:text-base">Actualiser</span>
        {:else}
          {#if canScan}
            <span class="sm:text-base">Nouveau scan</span>
          {:else}
            <span class="sm:text-base">Scan disponible demain</span>
          {/if}
        {/if}
      </Button>
    </div>

    {#if status === "Terminé"}
    <div class="results grid grid-cols-1 sm:grid-cols-2 gap-2 bg-white">
      {#if scanResult}
        {#each Array.from(groupedResults.entries()) as [type, results]}
          <div class="result-group flex flex-col max-h-48 gap-2 p-4 bg-white shadow overflow-auto">
            <p class="text-medium font-semibold">{getTypeTranslation(type)}</p>
            {#each results as result}
              <p class="text-sm italic text-gray-600">{result.Data}</p>
            {/each}
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
      <div class="relative color-bar h-4 w-full rounded">
        <div class="absolute h-7 w-0.5 top-[50%] translate-y-[-50%] bg-accent" style="left: {myScore*10}%"></div>
      </div>
    </div>
    {/if}
    
    {/if}
  </div>

  


<style>
  .light-bg{
    background-color: #f4f2fd;
  }
  .color-bar {
    background: linear-gradient(to left, rgb(255, 100, 100), rgb(255, 255, 100), rgb(100, 255, 100));
  }
</style>
   