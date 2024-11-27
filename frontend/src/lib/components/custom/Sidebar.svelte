<script>
	import Button from "../ui/button/button.svelte";
    import ChevronsLeft from "lucide-svelte/icons/chevrons-left";
    import ChevronsRight from "lucide-svelte/icons/chevrons-right";
    import 'iconify-icon';

    let sidebar = true;
    let sidebar_width = 'w-52';
    let style_link = 'px-5';
    let style_a = '';
    function handleClick() {
        sidebar = !sidebar;
        sidebar_width = sidebar ? 'w-52' : 'w-16';
        style_link = sidebar ? 'px-5' : '';
        style_a = sidebar ? '' : 'justify-center';
    }
</script>

<aside class={`h-full flex flex-col bg-secondary ${sidebar_width} justify-between`}>
        <div class="sidebar-header">
            <h3 class="text-white">Menu</h3>
        </div>
        <ul class={`flex flex-col gap-8 ${style_link}`}>
            <li>
                <a href="/dashboard" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:grid-2-fill"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">Dashboard</span>
                    {/if}
                </a>
            </li>
            <li>
                <a href="/phishing" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:fish-fill"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">Phishing</span>
                    {/if}
                </a>
            </li>
            <li>
                <a href="/osint" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:search-3-line"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">Osint</span>
                    {/if}
                </a>
            </li>
            <li>
                <a href="/formation" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:book-6-fill"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">Formation</span>
                    {/if}
                </a>
            </li>
            <li>
                <a href="/password" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:safe-lock-fill"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">Mot de passe</span>
                    {/if}
                </a>
            </li>
            <li>
                <a href="/about" class={`flex items-center gap-3 ${style_a}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:information-fill"></iconify-icon>
                    {#if sidebar}
                        <span class="text-white text-base font-semibold">A propos</span>
                    {/if}
                </a>
            </li>
        </ul>
        <Button class="bg-accent rounded-none py-6 mt-12" on:click={handleClick}>
            {#if sidebar}
                <ChevronsLeft class="text-white" />
            {:else}
                <ChevronsRight class="text-white" />
            {/if}
        </Button>

</aside>

<style>
    aside{
        transition: width 0.3s ease;
    }
    .icon-custom {
        color: white;
        font-size: 1.6rem; 
    }
    span{
        height: 1.5rem;
        word-break: break-all;
        overflow: hidden;
        animation: appear 0.8s ease;
    }
    @keyframes appear{
        0%{
            opacity: 0;
        }
        100%{
            opacity: 1;
        }
    }
</style>
