<script lang="ts">
  import Separator from "$lib/components/custom/Separator.svelte";
  import Button from "$lib/components/ui/button/button.svelte";
  import { onMount } from "svelte";
	import FormationVideoPopup from "$lib/components/custom/formation/FormationVideoPopup.svelte";

  let videos: any[] = [];
  let quiz: any[] = [];
  let loading_data = true;

  onMount(async () => {
    try {
      const fetchedVideos = await fetch("/api/formation/videos").then(res => res.json());

      // Update thumbnail URLs to point to our proxy endpoint:
      videos = fetchedVideos.map((video: any) => {
        video.thumbnailUrl = `/api/formation/asset?assetURL=${video.thumbnailUrl}`;
        return video;
      });

      quiz = await fetch("/api/formation/quiz").then(res => res.json());
    } catch (error) {
      console.error("Erreur lors de la récupération des vidéos et quiz:", error);
    } finally {
      loading_data = false;
    }
  });
</script>

<div class="relative z-10 flex flex-col w-full py-5 px-5 sm:py-6 sm:px-8">
  <div class="flex items-center gap-1">
    <iconify-icon class="me-2 text-2xl" icon="mingcute:video-camera-fill" style="color: #9082EC"></iconify-icon>
    <h1 class="text-xl font-semibold">Nos vidéos</h1>
  </div>

  <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>
  <div class="video_container w-full grid grid-cols-1 md:grid-cols-3 gap-16 my-8">
    {#each videos as video}
      <FormationVideoPopup video={video} />
    {/each}
  </div>

  <!-- <Button class="bg-accent w-full md:w-1/5 mx-auto mb-3">Voir tout</Button> -->
  <Separator color="bg-muted" width="w-full" margin_top="mt-6" margin_bottom="mb-6"/>

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
