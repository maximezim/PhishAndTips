<script lang="ts">
  import * as Card from "$lib/components/ui/card/index.js";
  import Particles from "$lib/components/magicUi/Particles.svelte";
  import logo_svg from '$lib/assets/images/pl_logo_pm.svg';

  let style_logo = 'w-12 h-12 mt-7';
  export let data;
  export let FormComponent;
  export let title;

  let particlesLoaded = false; // Indique si Particles a terminé son chargement

  const onParticlesLoaded = () => {
    particlesLoaded = true;
  };
</script>

<div class={"relative flex h-screen w-full items-center justify-center"}>
  <!-- Superposition qui bloque les interactions tant que particlesLoaded est false -->
  {#if !particlesLoaded}
    <div class="absolute inset-0 bg-white opacity-50 z-50"></div>
  {/if}

  <Particles
    className="absolute inset-0 -z-10"
    on:loaded={onParticlesLoaded} />
    
  <Card.Root class="min-w-96">
    <Card.Header class={"flex items-center pt-5 pb-3"}>
      <Card.Title class={"text-center text-2xl text-primary flex flex-col items-center"}>
        <img src={logo_svg} class={style_logo} alt="Logo de Phish&Tips" />
        <span>{title}</span>
      </Card.Title>
    </Card.Header>
    <Card.Content>
      <FormComponent data={data.form} />
    </Card.Content>
  </Card.Root>
</div>
