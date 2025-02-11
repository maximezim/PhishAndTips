<script lang="ts">
  import Separator from "$lib/components/custom/Separator.svelte";
  import Button from "$lib/components/ui/button/button.svelte";
  import { onMount } from "svelte";
	import FormationVideosCard from "$lib/components/custom/formation/FormationVideosCard.svelte";

  export let videos: any[] = [];
  
  let quiz: any[] = [];
</script>

<div class="relative z-10 flex flex-col w-full">
  <FormationVideosCard videos={videos} />

  <div class="flex items-center gap-1">
    <iconify-icon class="me-2 text-2xl" icon="mingcute:bookmark-fill" style="color: #9082EC"></iconify-icon>
    <h1 class="text-xl font-semibold">Nos quiz</h1>
  </div>

  <div class="flex justify-end gap-3">
    <Button class="bg-accent" href="formation/edit-quiz">Modifier un quiz</Button>
  </div>

  <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

  <div class="quiz_container w-full overflow-x-auto flex flex-col flex-nowrap md:flex-row gap-6 my-8 p-2">
    {#each quiz as q}
      <div class="bg-muted min-w-[300px] h-[370px] rounded shadow"></div>
    {/each}
  </div>
</div>