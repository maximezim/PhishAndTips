<script lang="ts">
	import FormationVideosCard from "$lib/components/custom/formation/FormationVideosCard.svelte";
	import FormationQuizCard from "$lib/components/custom/formation/FormationQuizCard.svelte";

  export let videos: any[] = [];
  export let quizzes: any[] = [];
</script>

<div class="relative z-10 flex flex-col w-full gap-6">
  <FormationVideosCard videos={videos} />
  <FormationVideosCard videos={videos} />
</div>