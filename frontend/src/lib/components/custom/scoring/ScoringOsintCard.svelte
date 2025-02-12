<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import 'iconify-icon';
  import type { ParsedResult, ParsedData, ExportResult, ScanResult, MyScan, UserScan } from '$types/osint';
  import { typeTranslations } from '$types/osint';
	import { onMount } from 'svelte';

  export let admin : boolean | null = false;
  export let userEmail : string | null = "";
  let scan: MyScan;
  let scanResult: ScanResult | null = null;
  let groupedResults = new Map<string, ParsedResult[]>();
  let dateScan = "";
  let status = "";
  let nbResults = "";

  onMount(async () => {
    if(admin){
      await getUserScan();
    }else{
      await getMyScan();
    }
  });

  async function getMyScan(){
    try {
      scan = await fetch("/api/osint/user").then(res => res.json());
    } catch(e) {
      console.error('Error while calling svelte my scan API: ', e);
    } finally{
      setValues();
    }
  }

  async function getUserScan(){
    try {
      const groupJson = {
        email : userEmail
      };
      scan = await fetch(`/api/osint/admin/get-scan`, {
          method: 'POST',
          body: JSON.stringify(groupJson),
          headers: {
            'Content-Type': 'application/json'
          }
      }).then(res => res.json());
    } catch(e) {
      console.error('Error while calling svelte user scan API: ', e);
    } finally{
      setValues();
    }
  }

  async function setValues(){
    if(Object.keys(scan).length > 0){
    scanResult = JSON.parse(scan.result);
    const dateStr = scan.updatedAt;
    const date = new Date(dateStr);
    const options: Intl.DateTimeFormatOptions = { 
      day: '2-digit', 
      month: 'long', 
      year: 'numeric', 
      hour: '2-digit', 
      minute: '2-digit',
      hour12: false
    };
    switch(scan.status){
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

<Card.Root class="col-span-4 row-span-5 flex flex-col h-full">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Osint</span>
      <iconify-icon class="text-3xl text-accent" icon="mingcute:search-3-line"></iconify-icon>
      </Card.Title>
  </Card.Header>
  <Card.Content class="flex-grow">
      {#if scan }
        <div class="flex flex-col text-left gap-2">
          <p class="text-medium font-semibold">Status : {status} {nbResults}</p>
          <p class="text-sm italic text-gray-600">{dateScan}</p>
        </div>
        <div class="max-h-[35vw] mt-6 overflow-auto h-full">
          {#if groupedResults}
            {#each Array.from(groupedResults.entries()) as [type, results]}
              <div class="result-group flex flex-col mt-2 gap-2 p-4 bg-gray-50 shadow overflow-auto">
                <p class="text-medium font-semibold">{getTypeTranslation(type)}</p>
                {#each results as result}
                  <p class="text-sm italic text-gray-600">{result.Data}</p>
                {/each}
              </div>

              <div class="result-group flex flex-col mt-2 gap-2 p-4 bg-gray-50 shadow overflow-auto">
                <p class="text-medium font-semibold">{getTypeTranslation(type)}</p>
                {#each results as result}
                  <p class="text-sm italic text-gray-600">{result.Data}</p>
                {/each}
              </div>

            {/each}
          {/if}
        </div>
        {:else}
        <p class="text-medium font-semibold">Aucun scan effectué</p>
      {/if}
  </Card.Content>
</Card.Root>