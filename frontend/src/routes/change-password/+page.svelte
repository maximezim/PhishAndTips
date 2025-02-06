<script lang="ts">
  import type { PageData } from "./$types.js";
  import LoginForm from "./change-password-form.svelte";
  import ConnectionCard from "$lib/components/custom/ConnectionCard.svelte";
  export let data: PageData;
</script>

<ConnectionCard data={data} FormComponent={LoginForm} title="Nouveau mot de passe" />