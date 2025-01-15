<script lang="ts">
  import type { PageData } from "./$types.js";
  import LoginForm from "./login-form.svelte";
  import ConnectionCard from "$lib/components/custom/ConnectionCard.svelte";
  export let data: PageData;
</script>

<ConnectionCard data={data} FormComponent={LoginForm} title="Connexion" />