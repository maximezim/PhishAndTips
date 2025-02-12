<script lang="ts">
  import { Button } from '$lib/components/ui/button';
  import { AspectRatio } from '$lib/components/ui/aspect-ratio';
  import type { Quiz } from '$types/formation';
	import { goto } from '$app/navigation';

  export let quiz: Quiz = {
    id: 0,    
    title: "",
    description: "",
    json: "",
  }
  
</script>

<Button on:click={() => goto(`/formation/quiz/${quiz.id}`)} class="w-full h-full p-0 shadow bg-white cursor-pointer border-2 flex flex-col justify-start items-start text-left hover:bg-white hover:border-accent" >
  <AspectRatio ratio={16/9} class="bg-muted flex flex-row overflow-hidden w-full items-center justify-center p-6">
    <h3 class="text-xl text-center text-black font-semibold w-full break-words text-wrap">
      {quiz.title}
    </h3>
  </AspectRatio>
  
  <div class="flex flex-col gap-1 py-2 px-5 justify-start items-start w-full">
    <p class="text-sm text-left text-black truncate w-full">{quiz.description}</p>
  </div>
</Button>