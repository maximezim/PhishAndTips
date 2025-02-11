<script lang="ts">
  export let score: number = 0;
</script>



<div class="relative color-bar h-4 w-full rounded">
  <div class="absolute h-7 w-1 top-[50%] translate-y-[-50%] bg-primary rounded-xl" style="left: {score}%"></div>
</div>


<style>
  .color-bar {
    background: linear-gradient(to left, rgb(255, 100, 100), rgb(255, 255, 100), rgb(100, 255, 100));
  }
</style>