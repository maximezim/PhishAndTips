<script lang="ts">
	import FormationVideosCard from "$lib/components/custom/formation/FormationVideosCard.svelte";
	import FormationQuizCard from "$lib/components/custom/formation/FormationQuizCard.svelte";

  export let videos: any[] = [];
  export let quizzes: any[] = [];
  export let canAddVideo: boolean = true;
  export let canAddQuiz: boolean = true;
</script>

<div class="relative z-10 flex flex-col w-full gap-6">
  <FormationVideosCard videos={videos} canAddVideo={canAddVideo} />
  <FormationQuizCard quizzes={quizzes} canAddQuiz={canAddQuiz} />
</div>