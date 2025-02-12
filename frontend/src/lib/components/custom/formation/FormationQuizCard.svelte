<script lang="ts">
  import * as Carousel from '$lib/components/ui/carousel';
  import * as Card from '$lib/components/ui/card';
	import type { Quiz } from '$types/formation';
  import 'iconify-icon';
	import UserAddPopup from '../admin/UserAddPopup.svelte';
	import FormationQuizButton from './FormationQuizButton.svelte';

  export let canModify: boolean = false;
  export let quizzes: Quiz[];
</script>

<Card.Root class="col-span-10 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <div class="flex flex-row gap-2 align-middle">
        <iconify-icon class="text-3xl text-accent flex flex-col align-middle" icon="mingcute:list-check-2-fill"></iconify-icon>
        <span>Quiz</span>
      </div>
      {#if canModify}
        <UserAddPopup />
      {/if}
    </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6 px-16">
    <Carousel.Root opts={{ align: "start" }} class="w-full h-full">
      <Carousel.Content class="h-full flex items-center">
        {#each quizzes as quiz}
          <Carousel.Item class="flex items-center justify-center md:basis-1/2 lg:basis-1/4 gap-10">
            <FormationQuizButton quiz={quiz} />
          </Carousel.Item>
        {/each}
      </Carousel.Content>
      <Carousel.Previous />
      <Carousel.Next />
    </Carousel.Root>
  </Card.Content>
</Card.Root>
