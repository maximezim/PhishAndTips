<script lang="ts">
    import Separator from "$lib/components/custom/Separator.svelte";
	import Button from "$lib/components/ui/button/button.svelte";
    import 'iconify-icon';
</script>

<div class="relative z-10 flex flex-col w-full py-6 px-8">

    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl" icon="mingcute:video-camera-fill" style="color: #9082EC"></iconify-icon>
        <h1 class="text-2xl font-semibold">Nos vidéos</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    <div class="video_container w-full grid grid-cols-1 md:grid-cols-2 gap-6 my-8">
        <div class="video h-80 w-full rounded shadow"></div>
        <div class="video h-80 rounded shadow"></div>
        <div class="video h-80 rounded shadow"></div>
        <div class="video h-80 rounded shadow"></div>
    </div>

    <Button class="bg-accent w-full md:w-1/5 mx-auto mb-3">Voir tout</Button>
    <Separator color="bg-muted" width="w-full" margin_top="mt-6" margin_bottom="mb-6"/>

    <div class="flex items-center gap-1">
        <iconify-icon class="me-2 text-2xl" icon="mingcute:bookmark-fill" style="color: #9082EC"></iconify-icon>
        <h1 class="text-2xl font-semibold">Nos quiz</h1>
    </div>

    <Separator color="bg-accent" width="w-1/5" margin_top="mt-3"/>

    <div class="quiz_container w-full overflow-x-auto flex flex-col flex-nowrap md:flex-row gap-6 my-8">
        <div class="quiz min-w-[300px] h-[350px] rounded shadow"></div>
        <div class="quiz min-w-[300px] h-[350px] rounded shadow"></div>
        <div class="quiz min-w-[300px] h-[350px] rounded shadow"></div>
        <div class="quiz min-w-[300px] h-[350px]  rounded shadow"></div>
        <div class="quiz min-w-[300px] h-[350px]  rounded shadow"></div>
        <div class="quiz min-w-[300px] h-[350px]  rounded shadow"></div>
    </div>


</div>

<style>
    .quiz, .video {
        background-color: #F1EBF6;
    }
</style>