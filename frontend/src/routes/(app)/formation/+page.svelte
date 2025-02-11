<script lang="ts">
    import Separator from "$lib/components/custom/Separator.svelte";
    import Button from "$lib/components/ui/button/button.svelte";
    import { AspectRatio } from "$lib/components/ui/aspect-ratio";
    import Plyr from 'plyr';
    import { onMount } from "svelte";

    let videos = [
        { title: "Titre de la vidéo", url: "/var/formation/data/videos/videos/Mise en contexte.mkv", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre de la vidéo", url: "https://www.youtube.com/embed/2", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre de la vidéo", url: "https://www.youtube.com/embed/3", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre de la vidéo", url: "https://www.youtube.com/embed/4", thumbnail: "https://via.placeholder.com/150" }
    ];

    let quiz = [
        { title: "Titre du quiz", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre du quiz", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre du quiz", thumbnail: "https://via.placeholder.com/150" },
        { title: "Titre du quiz", thumbnail: "https://via.placeholder.com/150" }
    ];

    let selectedVideo: { title: string; url: string; thumbnail: string } | null = null;

    function playVideo(video: { title: string; url: string; thumbnail: string }) {
        selectedVideo = video;
    }

    onMount(() => {
        new Plyr("#player");
    });
</script>

<div class="relative z-10 flex flex-col w-full py-5 px-5 sm:py-6 sm:px-8">
    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl text-purple-500" icon="mingcute:video-camera-fill"></iconify-icon>
        <h1 class="text-xl font-semibold">Nos vidéos</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    {#if selectedVideo}
        <div class="w-full flex flex-col items-center my-8">
            <div class="w-full max-w-4xl">
                <AspectRatio ratio={16 / 9} class="bg-gray-200 rounded-lg shadow-lg">
                    <iframe id="player" class="w-full h-full rounded-lg" src={selectedVideo.url} title={selectedVideo.title} frameborder="0" allowfullscreen></iframe>
                </AspectRatio>
            </div>
            <h2 class="mt-4 text-lg font-semibold text-center">{selectedVideo.title}</h2>
            <Button class="mt-4 bg-purple-500 text-white px-4 py-2 rounded-lg" on:click={() => selectedVideo = null}>Retour aux vidéos</Button>
        </div>
    {:else}
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6 my-8">
            {#each videos as video}
                <div class="cursor-pointer" on:click={() => playVideo(video)}>
                    <AspectRatio ratio={16 / 9} class="bg-gray-200 rounded-lg shadow-lg overflow-hidden">
                        <img src={video.thumbnail} alt={video.title} class="rounded-lg object-cover w-full h-full" />
                    </AspectRatio>
                    <p class="mt-2 text-center font-medium text-gray-700">{video.title}</p>
                </div>
            {/each}
        </div>
    {/if}

    <Separator color="bg-muted" width="w-full" margin_top="mt-6" margin_bottom="mb-6"/>

    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl text-purple-500" icon="mingcute:bookmark-fill"></iconify-icon>
        <h1 class="text-xl font-semibold">Nos quiz</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    <div class="quiz_container w-full overflow-x-auto flex flex-col md:flex-row gap-6 my-8 p-2">
        {#each quiz as q}
            <div class="bg-gray-200 min-w-[300px] h-[370px] rounded-lg shadow-lg flex flex-col items-center p-4">
                <img src={q.thumbnail} alt={q.title} class="rounded-lg object-cover w-full h-[200px]" />
                <p class="mt-4 text-center font-medium text-gray-700">{q.title}</p>
            </div>
        {/each}
    </div>
</div>
