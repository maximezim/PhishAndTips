<script lang="ts">
    import { onMount, onDestroy } from "svelte";
    export let src: string;
    export let alt: string = "";
    export let className: string = "";
    export let token: string;

    let objectUrl: string = "";

    onMount(async () => {
        try {
            const response = await fetch(src, {
                headers: {
                    "Authorization": "Bearer " + token
                }
            });
            if (!response.ok) {
                throw new Error(`Failed to load image: ${response.statusText}`);
            }
            const blob = await response.blob();
            objectUrl = URL.createObjectURL(blob);
        } catch (error) {
            console.error(error);
        }
    });

    onDestroy(() => {
        if (objectUrl) {
            URL.revokeObjectURL(objectUrl);
        }
    });
</script>

<img {alt} class={className} src={objectUrl} />