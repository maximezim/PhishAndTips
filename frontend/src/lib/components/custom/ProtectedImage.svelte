<script lang="ts">
    import { onMount, onDestroy } from "svelte";
    export let src: string;
    export let alt: string = "";
    export let className: string = "";
    export let token: string;

    let objectUrl: string = "";

    let loading = true;
    let error: Error | null = null;
    let retries = 0;
    const MAX_RETRIES = 3;

    async function loadImage() {
        loading = true;
        error = null;
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
        } catch (err) {
            error = err as Error;
            if (retries < MAX_RETRIES) {
                retries++;
                setTimeout(loadImage, 1000 * retries);
            }
        } finally {
            loading = false;
        }
    }

    onMount(loadImage);

    onDestroy(() => {
        if (objectUrl) {
            URL.revokeObjectURL(objectUrl);
        }
    });
</script>

<img {alt} class={className} src={objectUrl} />