<script lang="ts">
  import * as Tabs from '$lib/components/ui/tabs';
  import { onMount } from 'svelte';
	import FormationUserTab from '$lib/components/custom/formation/FormationUserTab.svelte';
	import FormationVideosCard from '$lib/components/custom/formation/FormationVideosCard.svelte';
	import type { Video, Quiz } from '$types/formation';

  let canGetAllUsers: boolean = false;
  let videos: Video[] = [];
  let quizzes: Quiz[] = [];
  let loading_data = true;

  onMount(async () => {
    try {
      canGetAllUsers = await fetch("/api/can-access/can-get-all-users").then(res => res.json());
      const fetchedVideos = await fetch("/api/formation/videos").then(res => res.json());

      // Update thumbnail URLs to point to our proxy endpoint:
      videos = fetchedVideos.map((video: any) => {
        video.thumbnailUrl = `/api/formation/asset?assetURL=${video.thumbnailUrl}`;
        video.captionUrl = `/api/formation/asset?assetURL=${video.captionUrl}`;
        video.videoUrl = `/api/formation/asset?assetURL=${video.videoUrl}`;
        return video;
      });

      const fetchedQuizzes = await fetch("/api/formation/quiz").then(res => res.json());

      quizzes = fetchedQuizzes.map((quiz: any) => {
        quiz.id = quiz.id;
        quiz.title = JSON.parse(quiz.json)['title'];
        quiz.description = JSON.parse(quiz.json)['description'];
        quiz.json = quiz.json;
        return quiz;
      });
    } catch (error) {
      console.error("Erreur lors de la récupération des vidéos et quiz:", error);
    } finally {
      loading_data = false;
    }
  });
</script>

<main class="relative z-10 flex flex-1 flex-col flex-grow gap-4 p-4 md:gap-8 md:p-8">
  <Tabs.Root value="user">
    <!-- If the user is Admin, it can see its data and access to admin panel -->
    {#if canGetAllUsers}
      <Tabs.List class="grid max-w-sm grid-cols-2">
        <Tabs.Trigger value="user">Perso</Tabs.Trigger>
        <Tabs.Trigger value="admin">Admin</Tabs.Trigger>
      </Tabs.List>
    {/if}

    <!-- User tab -->
    <Tabs.Content value="user">
      <FormationUserTab videos={videos} quizzes={quizzes} />
    </Tabs.Content>
  
    <!-- Admin tab -->
    <Tabs.Content value="admin" >
      <div class="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-10 w-full">
        <FormationVideosCard videos={videos} canModify={true} />
      </div>  
    </Tabs.Content>
  </Tabs.Root>
  
</main>


