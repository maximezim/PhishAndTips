<script lang="ts">
    import Separator from "$lib/components/custom/Separator.svelte";
	import Button from "$lib/components/ui/button/button.svelte";
    import {AspectRatio} from "$lib/components/ui/aspect-ratio";

    let videos = [
        {
            title: "Titre de la vidéo",
            url: "https://www.youtube.com/embed/1",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre de la vidéo",
            url: "https://www.youtube.com/embed/2",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre de la vidéo",
            url: "https://www.youtube.com/embed/3",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre de la vidéo",
            url: "https://www.youtube.com/embed/4",
            thumbnail: "https://via.placeholder.com/150"
        }
    ];

    let quiz = [
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/1",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/2",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/3",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/4",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/4",
            thumbnail: "https://via.placeholder.com/150"
        },
        {
            title: "Titre du quiz",
            url: "https://www.youtube.com/embed/4",
            thumbnail: "https://via.placeholder.com/150"
        }
    ]

</script>

<div class="relative z-10 flex flex-col w-full py-5 px-5 sm:py-6 sm:px-8">

    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl" icon="mingcute:video-camera-fill" style="color: #9082EC"></iconify-icon>
        <h1 class="text-xl font-semibold">Nos vidéos</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    <div class="video_container w-full grid grid-cols-1 md:grid-cols-2 gap-6 my-8">
        {#each videos as video}
            <AspectRatio ratio={16 / 9} class="bg-muted rounded shadow">
                <!--<img src="..." alt="..." class="rounded-md object-cover" />-->
              </AspectRatio>
        {/each}
    </div>

    <Button class="bg-accent w-full md:w-1/5 mx-auto mb-3">Voir tout</Button>
    <Separator color="bg-muted" width="w-full" margin_top="mt-6" margin_bottom="mb-6"/>

    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl" icon="mingcute:bookmark-fill" style="color: #9082EC"></iconify-icon>
        <h1 class="text-xl font-semibold">Nos quiz</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    <div class="quiz_container w-full overflow-x-auto flex flex-col flex-nowrap md:flex-row gap-6 my-8 p-2">
        {#each quiz as q}
            <div class="bg-muted min-w-[300px] h-[370px] rounded shadow"></div>
        {/each}
    </div>


</div>
