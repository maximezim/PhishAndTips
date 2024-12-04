<script lang="ts">
  import * as Card from "$lib/components/ui/card/index.js";
  import Particles from "$lib/components/magicUi/Particles.svelte";
  import type { PageData } from "./$types.js";
  import LoginForm from "./login-form.svelte";
  export let data: PageData;
</script>

<div class={"flex h-screen w-full items-center justify-center"}>
  <Particles className="absolute inset-0 -z-10" />
  <Card.Root class="min-w-96">
    <Card.Header class={"flex items-center pt-5 pb-5"}>
      <Card.Title class={"text-center text-3xl text-primary"}>
        <span>Connexion</span>
      </Card.Title>
    </Card.Header>
    <Card.Content>
      <LoginForm data={data.form} />
    </Card.Content>
  </Card.Root>
</div>   
