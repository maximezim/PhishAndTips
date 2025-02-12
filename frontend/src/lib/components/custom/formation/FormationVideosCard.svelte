<script lang="ts">
  import * as Carousel from '$lib/components/ui/carousel';
  import * as Card from '$lib/components/ui/card';
	import type { Video } from '$types/formation';
  import 'iconify-icon';
	import FormationVideoPopup from '$lib/components/custom/formation/FormationVideoPopup.svelte';
	import VideoAddPopup from '$lib/components/custom/admin/VideoAddPopup.svelte';

  export let videos: Video[];
  export let canAddVideo: boolean = false;
</script>

<Card.Root class="col-span-10 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <div class="flex flex-row gap-2 align-middle">
        <iconify-icon class="text-3xl text-accent flex flex-col align-middle" icon="mingcute:video-fill"></iconify-icon>
        <span>Vidéos</span>
      </div>
      {#if canAddVideo}
        <VideoAddPopup />
      {/if}
    </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6 px-16">
    <Carousel.Root opts={{ align: "start" }} class="w-full h-full">
      <Carousel.Content class="h-full flex items-center">
        {#each videos as video}
          <Carousel.Item class="flex items-center justify-center md:basis-1/2 lg:basis-1/4 gap-10">
            <FormationVideoPopup video={video} canDelVideo={canAddVideo} />
          </Carousel.Item>
        {/each}
      </Carousel.Content>
      <Carousel.Previous />
      <Carousel.Next />
    </Carousel.Root>
  </Card.Content>
</Card.Root>
