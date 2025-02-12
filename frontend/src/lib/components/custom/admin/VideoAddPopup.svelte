<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
	import type { Video } from "$types/formation";
	import { Input } from "$lib/components/ui/input";
	import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";
  
  let video: Video = {
    id: 0,
    title: "",
    description: "",
    videoUrl: "",
    captionUrl: "",
    thumbnailUrl: "",
  };

  let errors = {
    title: "",
    description: "",
    videoFile: "",
    thumbnailFile: "",
  };

  let captionsFile: File|null = null;
  let thumbnailFile: File|null = null;
  let videoFile: File|null = null;

  function validateForm(): boolean {
    let isValid = true;

    if (!video.title.trim()) {
      errors.title = "Le titre est obligatoire.";
      isValid = false;
    } else {
      errors.title = "";
    }

    if (!video.description.trim()) {
      errors.description = "La description est obligatoire.";
      isValid = false;
    } else {
      errors.description = "";
    }

    if (!videoFile) {
      errors.videoFile = "La video est obligatoire.";
      isValid = false;
    } else {
      errors.videoFile = "";
    }

    if (!thumbnailFile) {
      errors.thumbnailFile = "La miniature est obligatoire.";
      isValid = false;
    } else {
      errors.thumbnailFile = "";
    }

    return isValid;
  }

  async function createUser() {
    if (!validateForm()){
      console.error(errors);
      return;
    }

    await fetch('/api/formation/video', {
			method: 'POST',
			body: JSON.stringify(video),
			headers: {
				'Content-Type': 'application/json'
			}
		});
    closeAlertDialog();
  }

  function closeAlertDialog() {
    window.location.reload();
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class={"bg-accent py-0 px-3 text-accent-foreground flex flex-row align-middle gap-1"} builders={[builder]}>
      Ajouter
      <iconify-icon class="icon-custom" icon="mingcute:add-circle-line"></iconify-icon>
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
    <AlertDialog.Header>
      <AlertDialog.Title>Ajouter une vidéo</AlertDialog.Title>
    </AlertDialog.Header>
    <div class="grid grid-cols-1 p-1 w-full gap-x-8 gap-y-4 pt-5 overflow-auto">
      <!-- Title -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer un titre</p>
        <Input type="text" bind:value={video.title} placeholder="Titre" class="w-full" />
        {#if errors.title}
          <p class="text-red-500 text-sm">{errors.title}</p>
        {/if}
      </div>

      <!-- Description -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Entrer une description</p>
        <Input type="text" bind:value={video.description} placeholder="Description" class="w-full" />
        {#if errors.description}
          <p class="text-red-500 text-sm">{errors.description}</p>
        {/if}
      </div>

      <!-- Video File -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Vidéo. Format accepté : mp4</p>
        <Input
          type="file"
          accept=".mp4"
          class="w-full"
          on:change="{(e) => {
            videoFile = (e.target! as HTMLInputElement).files![0];
          }}"
        />

        {#if errors.videoFile}
          <p class="text-red-500 text-sm">{errors.videoFile}</p>
        {/if}
      </div>

      <!-- Thumbnail File -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Miniature. Format accepté : png, jpg, jpeg</p>
        <Input
          type="file"
          accept=".png .jpg .jpeg"
          class="w-full"
          on:change="{(e) => {
            thumbnailFile = (e.target! as HTMLInputElement).files![0];
          }}"
        />

        {#if errors.thumbnailFile}
          <p class="text-red-500 text-sm">{errors.thumbnailFile}</p>
        {/if}
      </div>

      <!-- Captions File -->
      <div class="name flex flex-col gap-2">
        <p class="text-sm">Sous-titres. Format accepté : vtt</p>
        <Input
          type="file"
          accept=".mp4"
          class="w-full"
          on:change="{(e) => {
            thumbnailFile = (e.target! as HTMLInputElement).files![0];
          }}"
        />

        {#if errors.thumbnailFile}
          <p class="text-red-500 text-sm">{errors.thumbnailFile}</p>
        {/if}
      </div>
      
    </div>
    <AlertDialog.Footer>
      <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
      <ConfirmPopup description="Création de l'utilisateur" name="Ajouter" style="bg-accent" functionToCall={createUser} />
    </AlertDialog.Footer>
  </AlertDialog.Content>
</AlertDialog.Root>