<script lang="ts">
  import type { PageData } from "./$types.js";
  import LoginForm from "./login-form.svelte";
  import ConnectionCard from "$lib/components/custom/ConnectionCard.svelte";
  import AuthService from "$lib/services/AuthService.js";
	import { onMount } from "svelte";
	import { goto } from "$app/navigation";
  export let data: PageData;

  onMount(async () => {
    if (await AuthService.isLogged()) {
      goto("/dashboard");
    }
  });
</script>



<ConnectionCard data={data} FormComponent={LoginForm} title="Connexion" />