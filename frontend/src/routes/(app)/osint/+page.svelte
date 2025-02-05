<script lang='ts'>
    import OrbitingCircles from "$lib/components/magicUi/OrbitingCircles.svelte";
	  import Button from "$lib/components/ui/button/button.svelte";
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import 'iconify-icon';
    import { onMount } from 'svelte';

    import githubSvg from '$lib/assets/svg/github.svg';
    import facebookSvg from '$lib/assets/svg/facebook.svg';
    import googleSvg from '$lib/assets/svg/google.svg';
    import darkwebSvg from '$lib/assets/svg/darkweb.svg';
    import amazonSvg from '$lib/assets/svg/amazon.svg';
    import twitterSvg from '$lib/assets/svg/twitter.svg';

    let searching = false;
    let showRes = false;
    let orbite_container = "opacity-0";

    let orbites = [
      { name: "github", radius: 200, duration: 20, delay: 0, reverse: false, style: "opacity-0", svg: githubSvg },
      { name: "twitter", radius: 200, duration: 20, delay: -90, reverse: false, style: "opacity-0", svg: twitterSvg },
      { name: "facebook", radius: 270, duration: 40, delay: 0, reverse: true, style: "opacity-0", svg: facebookSvg },
      { name: "google", radius: 270, duration: 40, delay: -130, reverse: true, style: "opacity-0", svg: googleSvg },
      { name: "darkweb", radius: 340, duration: 40, delay: 0, reverse: false, style: "opacity-0", svg: darkwebSvg },
      { name: "amazon", radius: 340, duration: 30, delay: -180, reverse: false, style: "opacity-0", svg: amazonSvg },
    ];

    let isAdmin = false;

    onMount(async () => {
      try {
        isAdmin = await fetch("/api/auth/admin").then(res => res.json());
      } catch (error) {
        console.error("Erreur lors de la récupération des campagnes:", error);
      }
      finally {
        
      }
    });

    function searchOsint() {
      searching = true;
      orbite_container = "opacity-100";
      for (let i = 0; i < orbites.length; i++) {
        setTimeout(() => {
          orbites[i].style = 'opacity-100';
        }, (i + 1) * 400);
      }
      setTimeout(() => {
        showResults();
      }, 5000);
    }

    function showResults() {
      searching = false;
      showRes = true;
      for (let i = 0; i < orbites.length; i++) {
        orbites[i].style = 'opacity-0';
      }
      setTimeout(() => {
        orbite_container = "opacity-0";
      }, 500);
      console.log(showRes);
    }

    function closeResults(){
      showRes = false;
    }

  </script>

  <div class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-2 md:p-8">
    <div class="w-full p-6 light-bg flex items-center justify-between ">
      <div class="flex flex-col gap-2">
        <p class="text-lg font-semibold">Nombre de scan : 3</p>
        <p class="text-medium italic text-gray-600">Dernier scan : 10 Janvier 2025</p>
      </div>
      <Button class="bg-accent px-12 py-6 relative z-10"  on:click={searchOsint} disabled={searching}>
        {#if searching}
          <span class="text-base">Recherche en cours...</span>
        {:else}
          <span class="text-base">Nouveau scan</span>
        {/if}
      </Button>
    </div>

    <div class="scans flex gap-3">

      <div class="auto"></div>

      <div class="scan flex justify-between shadow items-center w-full gap-3 p-4 bg-white  hover:bg-gray-100">
        <div class="flex items-center gap-5">
          <div class="score rounded bg-green-300 flex items-center justify-center w-20 h-20">
            <p class="text-5xl text-white font-bold">A</p>
          </div>
          <div class="flex flex-col gap-1">
            <p class="text-lg font-semibold">Automatique</p>
            <p class="text-medium italic text-gray-600">Date : 10 Janvier 2025</p>
          </div>
        </div>
        <Button class="bg-accent rounded-full relative z-10" >
          <iconify-icon class="text-3xl" icon="mingcute:eye-2-fill"></iconify-icon>
        </Button>
      </div>

      <div class="scan flex items-center gap-3 p-4 bg-white shadow hover:bg-gray-100">
        <div class="flex items-center gap-5 w-full">
          <div class="score rounded bg-yellow-300 flex items-center justify-center w-20 h-20">
            <p class="text-5xl text-white font-bold">C</p>
          </div>
          <div class="flex flex-col gap-1">
            <p class="text-lg font-semibold">Manuel</p>
            <p class="text-medium italic text-gray-600">Date : 01 Janvier 2025</p>
          </div>
        </div>
        <Button class="bg-accent rounded-full relative z-10" >
          <iconify-icon class="text-3xl" icon="mingcute:eye-2-fill"></iconify-icon>
        </Button>
      </div>  

      <div class="scan flex items-center gap-3 p-4 bg-white shadow hover:bg-gray-100">
        <div class="flex items-center gap-5 w-full">
          <div class="score rounded bg-red-300 flex items-center justify-center w-20 h-20">
            <p class="text-5xl text-white font-bold">F</p>
          </div>
          <div class="flex flex-col gap-1">
            <p class="text-lg font-semibold">Automatique</p>
            <p class="text-medium italic text-gray-600">Date : 22 Novembre 2024</p>
          </div>
        </div>
        <Button class="bg-accent rounded-full relative z-10" >
          <iconify-icon class="text-3xl" icon="mingcute:eye-2-fill"></iconify-icon>
        </Button>
      </div>  

      
    </div>

  </div>









   <!--
  <div class="search relative flex w-full h-full items-center justify-center md:shadow-xl overflow-hidden">
    <div class="absolute w-full h-full bg-white opacity-60"></div>

    


 
    {#if showRes}
    <AlertDialog.Root open={showRes} on:close={closeResults}>
      <AlertDialog.Content class="w-4/5 max-w-none md:w-auto h-4/5 flex flex-col">
        <AlertDialog.Header>
          <AlertDialog.Title>Résulats de la recherche</AlertDialog.Title>
          <AlertDialog.Description>
            Voici une sélection de sites trouvés en lien avec votre recherche OSINT. 
            Notez que cette liste n'est pas exhaustive et peut ne pas inclure tous les résultats possibles.
          </AlertDialog.Description>
        </AlertDialog.Header>
        <div class="flex-grow overflow-y-auto">
          <div class="grid grid-cols-3 gap-5 my-4">
            {#each orbites as orbite, index}
              <div class="transition flex flex-col items-center gap-4 bg-muted/[.07] p-8 shadow hover:text-foreground hover:bg-muted hover:text-foreground">
                <img src={orbite.svg} alt={orbite.name} class="w-20 h-20" />
                <span>{orbite.name}</span>
              </div>
            {/each}
          </div>
        </div>
        <AlertDialog.Footer>
          <AlertDialog.Cancel on:click={closeResults}>Fermer</AlertDialog.Cancel>
        </AlertDialog.Footer>
      </AlertDialog.Content>
    </AlertDialog.Root>
    {/if}

    <Button class="bg-accent px-12 py-6 relative z-10"  on:click={searchOsint} disabled={searching}>
      <iconify-icon class="me-2 text-xl" icon="mingcute:search-2-line"></iconify-icon>
      {#if searching}
        <span>Recherche en cours...</span>
      {:else}
        <span>Recherche OSINT : {isAdmin}</span>
      {/if}
    </Button>
  
    <div class="orbite_container absolute flex items-center justify-center w-full h-full {orbite_container}">
    {#each orbites as orbite, index}
      <OrbitingCircles
        class={`h-[55px] w-[55px] border-none bg-transparent`}
        duration={orbite.duration}
        radius={orbite.radius}
        delay={orbite.delay}
        reverse={orbite.reverse}
      >
        <img src={orbite.svg} alt={orbite.name} class="orbiting-circles h-full w-full {orbite.style}" />
      </OrbitingCircles>
    {/each}
    </div>

  

  </div>
-->

  <style>
    .transition{
      transition: background-color 0.1s ease;
    }
    .orbite_container{
      transition: all 0.5s ease;
    }
    .orbiting-circles {
      transition: opacity 0.5s ease;
    }

    .light-bg{
      background-color: #f4f2fd;
    }
    .white-bg{
      background-color: rgb(254, 254, 254);
    }
  </style>
   