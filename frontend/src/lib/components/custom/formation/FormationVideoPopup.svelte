<script lang="ts">
  import { Button } from '$lib/components/ui/button';
  import * as AlertDialog from '$lib/components/ui/alert-dialog';
  import type { Video } from '$types/formation';
  import { AspectRatio } from '$lib/components/ui/aspect-ratio';

  export let video: Video = {
    id: 0,
    title: "",
    description: "",
    videoUrl: "",
    captionUrl: "",
    thumbnailUrl: "",
  };

  async function onVideoEnded() {
    await fetch('/api/formation/quiz', {
      method: 'POST',
      body: JSON.stringify({
        videoId: video.id
      }),
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class="w-full h-full p-0 shadow bg-white cursor-pointer border-2 flex flex-col justify-start items-start text-left hover:bg-white hover:border-accent" builders={[builder]}>
      <AspectRatio ratio={16/9} class="bg-muted overflow-hidden">
        <img src={video.thumbnailUrl} alt={video.title} class="w-full h-full object-cover" />
      </AspectRatio>
      <div class="flex flex-col gap-1 py-2 px-5 justify-start items-start w-full">
        <h3 class="text-lg text-left text-black font-semibold truncate w-full">{video.title}</h3>
        <p class="text-sm text-left text-black truncate w-full">{video.description}</p>
      </div>
    </Button>
  </AlertDialog.Trigger>

  <AlertDialog.Content class="max-w-5xl flex flex-col">
    <AlertDialog.Header class="flex flex-row justify-between gap-3">
      <div class="flex flex-col gap-2 text-left">
        <AlertDialog.Title>{video.title}</AlertDialog.Title>
        <AlertDialog.Description>
          {video.description}
        </AlertDialog.Description>
      </div>
      <AlertDialog.Cancel>Fermer</AlertDialog.Cancel>
    </AlertDialog.Header>


    <video controlslist="nodownload" playsinline controls data-poster={video.thumbnailUrl} on:ended={onVideoEnded}>
      <source src={video.videoUrl} type="video/mp4" />
      <track kind="captions" label="Français" src={video.captionUrl} srclang="fr" default />
    </video>
  </AlertDialog.Content>
</AlertDialog.Root>
