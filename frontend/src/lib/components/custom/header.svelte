<script lang="ts">
	import Button from "../ui/button/button.svelte";

    export let title: string;


</script>

<header class={"w-full py-3 px-4 sticky top-0 bg-primary flex items-center justify-between z-10"}>
    <h1 class={"text-primary-foreground text-2xl font-bold"}>{title}</h1>
    <Button variant="outline">Click me</Button>
</header>