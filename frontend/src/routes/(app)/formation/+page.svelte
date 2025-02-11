<script lang="ts">
  import Separator from "$lib/components/custom/Separator.svelte";
  import Button from "$lib/components/ui/button/button.svelte";
  import { AspectRatio } from "$lib/components/ui/aspect-ratio";
  import FormationService from '$lib/services/FormationService';
  import { onMount } from "svelte";
  import Cookies from 'js-cookie';

  let videos: any[] = [];
  let quiz: any[] = [];
  let loading_data = true;

  onMount(async () => {
    try {
      const fetchedVideos = await fetch("/api/formation/videos").then(res => res.json());

      videos = fetchedVideos.map((video: any) => {
        try {
          const urlParts = video.thumbnailUrl.split('/');
          if (urlParts.length === 0) {
            throw new Error('Invalid thumbnail URL');
          }
          const id = urlParts[urlParts.length - 1];
          video.thumbnailUrl = `/api/formation/thumbnail/${id}`;
          return video;
        } catch (error) {
          console.error(`Error processing thumbnail URL for video: ${video.title}`, error);
          video.thumbnailUrl = '/placeholder-thumbnail.jpg';
          return video;
        }
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
  <div class="video_container w-full grid grid-cols-1 md:grid-cols-3 gap-6 my-8">
    {#each videos as video}
      <AspectRatio ratio={20/9} class="bg-muted rounded shadow">
        <img src={video.thumbnailUrl} alt={video.title} class="rounded-md object-contain" />
      </AspectRatio>
    {/each}
  </div>

  <Button class="bg-accent w-full md:w-1/5 mx-auto mb-3">Voir tout</Button>
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
