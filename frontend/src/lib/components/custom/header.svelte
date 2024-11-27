<script lang="ts">
	import Button from "../ui/button/button.svelte";

    export let title: string;


</script>

<header class={"w-full py-3 px-4 bg-primary flex items-center justify-between"}>
    <h1 class={"text-white text-2xl"}>{title}</h1>
    <Button variant="outline">Click me</Button>
</header>