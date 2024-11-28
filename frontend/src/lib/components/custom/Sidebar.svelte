<script>
	import Button from "../ui/button/button.svelte";
    import ChevronsLeft from "lucide-svelte/icons/chevrons-left";
    import ChevronsRight from "lucide-svelte/icons/chevrons-right";
    import 'iconify-icon';
    import fish_svg from '$lib/assets/images/fish.svg';
    import logo_svg from '$lib/assets/images/pl_logo.svg';

    export let dash_text;
    export let dash_bg;
    export let about_text;
    export let about_bg;
    export let form_text;
    export let form_bg;
    export let osint_text;
    export let osint_bg;
    export let pwd_text;
    export let pwd_bg;
    export let phishing_text;
    export let phishing_bg;

    let sidebar = true;
    let sidebar_width = 'w-60';
    let style_link = 'px-5 py-3.5';
    let style_li = '';
    let style_logo = 'w-12 h-12 mt-7';
    function handleClick() {
        sidebar = !sidebar;
        sidebar_width = sidebar ? 'w-60' : 'w-16';
        style_link = sidebar ? 'px-5 py-3.5' : 'py-3.5';
        style_li = sidebar ? '' : 'justify-center';
        style_logo = sidebar ? 'w-12 h-12 mt-7' : 'w-8 h-8 mt-5';
    }


</script>

<aside class={`h-full flex flex-col bg-secondary ${sidebar_width} justify-between`}>
    <div class="sidebar-up flex flex-col items-center gap-16">
        <div class={`sidebar-header h-32 flex flex-col items-center gap-3`}>
            <img src={logo_svg} alt="logo" class={`logo ${style_logo}`} />
                {#if sidebar}
                    <h1 class={`logo_text text-white text-2xl font-semibold`}>Phish & Tips</h1>
                {/if}
        </div>
        <ul class={`flex flex-col gap-1 w-full`}>
            <a href="/dashboard" class={`bg-${dash_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:grid-2-fill" style={`color: ${dash_text}`}></iconify-icon>
                    {#if sidebar}
                        <span class={`text-${dash_text} text-base font-semibold`}>Dashboard</span>
                    {/if}
                </li>
            </a>

            <a href="/phishing" class={`bg-${phishing_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <img src={fish_svg} alt="fish" class="w-5 h-5 ms-1 fish" style={`filter: invert(${phishing_text === 'white' ? 1 : 0});`}  />
                    {#if sidebar}
                        <span class={`text-${phishing_text} text-base font-semibold`}>Phishing</span>
                    {/if}
                </li>
            </a>

            <a href="/osint" class={`bg-${osint_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:search-3-line" style={`color: ${osint_text}`}></iconify-icon>
                    {#if sidebar}
                        <span class={`text-${osint_text} text-base font-semibold`}>Osint</span>
                    {/if}
                </li>
            </a>

            <a href="/formation" class={`bg-${form_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:book-6-fill" style={`color: ${form_text}`}></iconify-icon>
                    {#if sidebar}
                        <span class={`text-${form_text} text-base font-semibold`}>Formation</span>
                    {/if}
                </li>
            </a>

            <a href="/password" class={`bg-${pwd_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:safe-lock-fill" style={`color: ${pwd_text}`}></iconify-icon>
                    {#if sidebar}
                        <span class={`text-${pwd_text} text-base font-semibold`}>Mot de passe</span>
                    {/if}
                </li>
            </a>

            <a href="/about" class={`bg-${about_bg} ${style_link}`}>
                <li class={`flex items-center gap-4 ${style_li}`}>
                    <iconify-icon class="icon-custom" icon="mingcute:information-fill" style={`color: ${about_text}`}></iconify-icon>
                    {#if sidebar}
                        <span class={`text-${about_text} text-base font-semibold`}>A propos</span>
                    {/if}
                </li>
            </a>
        </ul>
    </div>

    <Button class="bg-accent rounded-none py-6 mt-12" on:click={handleClick}>
        {#if sidebar}
            <ChevronsLeft class="text-white" />
        {:else}
            <ChevronsRight class="text-white" />
        {/if}
    </Button>

</aside>

<style>
    aside, ul{
        transition: width 0.3s ease;
    }
    .fish{
        transition: all 0.3s ease;
    }
    .icon-custom {
        font-size: 1.6rem; 
        transition: all 0.3s ease;
    }
    span{
        height: 1.5rem;
        word-break: break-all;
        overflow: hidden;
        animation: appear 0.8s ease;
    }
    .logo {
        transition: all 0.3s ease;
    }
    .logo_text{
        height: 2rem;
        overflow: hidden;
        word-break: break-all;
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
