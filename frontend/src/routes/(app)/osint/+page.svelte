<script lang='ts'>
    import OrbitingCircles from "$lib/components/magicUi/OrbitingCircles.svelte";
	  import Button from "$lib/components/ui/button/button.svelte";
    import * as AlertDialog from "$lib/components/ui/alert-dialog";
    import 'iconify-icon';

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

  <div class="relative flex w-full h-full items-center justify-center md:shadow-xl overflow-hidden">
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
        <span>Recherche OSINT</span>
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
  </style>
   