<script lang="ts">
  import ScoringGradientBar from "./ScoringGradientBar.svelte";

  export let title: string = "";
  export let score: number = 0;
</script>

<div class={"flex flex-col bg-white w-full p-6 gap-5"}>
  <h1 class={"text-m font-semibold"}>{title}</h1>
  <ScoringGradientBar score={score} />
</div>

