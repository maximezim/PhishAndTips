<script lang='ts'>
    import OrbitingCircles from "$lib/components/magicUi/OrbitingCircles.svelte";
	  import Button from "$lib/components/ui/button/button.svelte";
    import 'iconify-icon';

    import githubSvg from '$lib/assets/svg/github.svg';
    import facebookSvg from '$lib/assets/svg/facebook.svg';
    import googleSvg from '$lib/assets/svg/google.svg';
    import darkwebSvg from '$lib/assets/svg/darkweb.svg';
    import amazonSvg from '$lib/assets/svg/amazon.svg';
    import twitterSvg from '$lib/assets/svg/twitter.svg';

    let searching = false;
    let disabled = "";

    let orbites = [
      {
        name: "github",
        radius: 200,
        duration: 20,
        delay: 0,
        reverse: false,
        style: "opacity-0",
        svg: githubSvg
      },
      {
        name: "twitter",
        radius: 200,
        duration: 20,
        delay: -90,
        reverse: false,
        style: "opacity-0",
        svg: twitterSvg
      },
      {
        name: "facebook",
        radius: 270,
        duration: 40,
        delay: 0,
        reverse: true,
        style: "opacity-0",
        svg: facebookSvg
      },
      {
        name: "google",
        radius: 270,
        duration: 40,
        delay: -130,
        reverse: true,
        style: "opacity-0",
        svg: googleSvg
      },
      {
        name: "darkweb",
        radius: 340,
        duration: 40,
        delay: 0,
        reverse: false,
        style: "opacity-0",
        svg: darkwebSvg
      },
      {
        name: "amazon",
        radius: 340,
        duration: 30,
        delay: -180,
        reverse: false,
        style: "opacity-0",
        svg: amazonSvg
      },
    ];


    function searchOsint() {
      disabled = "disabled";
      searching = true;
      for (let i = 0; i < orbites.length; i++) {
        setTimeout(() => {
          orbites[i].style = 'opacity-100';
        }, (i + 1) * 700);
      }
    }
  
  </script>

  <div class="relative flex w-full h-full items-center justify-center md:shadow-xl overflow-hidden">
    <div class="absolute w-full h-full bg-white opacity-60"></div>
  
    <Button class="bg-accent px-12 py-6 relative z-10"  on:click={searchOsint} disabled={searching}>
      <iconify-icon class="me-1 text-xl" icon="mingcute:search-2-line"></iconify-icon>
      {#if searching}
        <span>Recherche en cours...</span>
      {:else}
        <span>Recherche OSINT</span>
      {/if}
    </Button>
  
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


  <style>
    .orbiting-circles {
      transition: opacity 0.5s ease;
    }
  </style>
  