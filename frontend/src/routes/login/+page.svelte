<script lang="ts">
  import { Button } from "$lib/components/ui/button/index.js";
  import * as Card from "$lib/components/ui/card/index.js";
  import { Input } from "$lib/components/ui/input/index.js";
  import Particles from "$lib/components/magicUi/Particles.svelte";
	import { goto } from "$app/navigation";

  function handleSubmit(event: { preventDefault: () => void; }) {
    event.preventDefault();
    goto("/");
  }
</script>

<div class={"flex h-screen w-full items-center justify-center"}>
  <Particles className="absolute inset-0 -z-10" />
  <Card.Root>
    <Card.Header class={"bg-primary flex items-center pt-5 pb-5"}>
      <Card.Title class={"text-center text-3xl text-primary-foreground"}>
        <span>Connexion</span>
      </Card.Title>
    </Card.Header>
    <Card.Content>
      <form class={"flex flex-col gap-5"}>
        <div>
          <label for="email" class={"text-lg"}>Adresse mail</label>
          <Input type="email" placeholder="example@mail.com" class={"sm:w-[400px] md:w-[300px] lg:w-[400px] text-base h-full p-2"} />  
        </div>

        <div>
          <label for="password" class={"text-lg"}>Mot de passe</label>
          <Input type="password" class={"sm:w-[400px] md:w-[300px] lg:w-[400px] text-base h-full p-2"} />
        </div>
        <Button type="submit" class={"text-lg h-full p-2"}> Se connecter </Button>
      </form>
    </Card.Content>
  </Card.Root>
</div>   
