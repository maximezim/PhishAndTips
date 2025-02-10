<script lang="ts">
    import 'iconify-icon';
    import fish_svg from '$lib/assets/images/fish.svg';

    export let dash_text;
    export let about_text;
    export let form_text;
    export let osint_text;
    export let pwd_text;
    export let phishing_text;
    export let className = "";


</script>

<div class="grid grid-cols-6 items-center w-full bg-primary h-[60px] fixed z-10 bottom-0 left-0 {className}">
    <a href="/dashboard" aria-label="Dashboard" class="{dash_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <iconify-icon class="icon-custom" icon="mingcute:grid-2-fill"></iconify-icon>
    </a>

    <a href="/phishing" aria-label="Phishing" class="{phishing_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <img src={fish_svg} alt="fish" class="fish ms-1"/>
    </a>

    <a href="/osint" aria-label="OSINT" class="{osint_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <iconify-icon class="icon-custom" icon="mingcute:search-3-line"></iconify-icon>
    </a>

    <a href="/formation" aria-label="Formation" class="{form_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <iconify-icon class="icon-custom" icon="mingcute:book-6-fill"></iconify-icon>
    </a>

    <a href="/password" aria-label="Password" class="{pwd_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <iconify-icon class="icon-custom" icon="mingcute:safe-lock-fill"></iconify-icon>
    </a>

    <a href="/about" aria-label="About" class="{about_text === 'white' ? '' : 'bg-accent'} h-full flex items-center justify-center">
        <iconify-icon class="icon-custom" icon="mingcute:information-fill"></iconify-icon>
    </a>

</div>

<style>
    a {
        transition: all 0.2s ease;
    }
    .icon-custom {
        font-size: 1.6rem; 
        color: white;
        transition: all 0.3s ease;
    }
    .fish{
        width: auto;
        height: 20px;
        filter: invert(1);
    }
</style>