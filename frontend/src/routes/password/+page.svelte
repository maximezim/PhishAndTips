<script lang="ts">
    import Separator from '$lib/components/custom/Separator.svelte';
    import 'iconify-icon';
    import zxcvbn from 'zxcvbn';

    let password = '';
    let level = "Très faible";
    let time = "moins d'une seconde";
    let result: any = null;
    let result_style: string = "bg-red-400";
    let uppercase: boolean = false;
    let number: boolean = false;
    let special: boolean = false;

    function translateTime(time: string): string {
        const translations: { [key: string]: string } = {
        'instant': 'instantané',
        'less than a second': 'moins d\'une seconde',
        'second': 'seconde',
        'seconds': 'secondes',
        'minute': 'minute',
        'minutes': 'minutes',
        'hour': 'heure',
        'hours': 'heures',
        'day': 'jour',
        'days': 'jours',
        'months': 'mois',
        'year': 'année',
        'years': 'années',
        'century': 'siècle',
        'centuries': 'Plusieurs siècles'
        };
        
        for (const [key, value] of Object.entries(translations)) {
        if (time.includes(key)) {
            return time.replace(key, value);
        }
        }
        return time;
    }

    function checkPassword() {
        result = zxcvbn(password);
        time = translateTime(result.crack_times_display.offline_slow_hashing_1e4_per_second);
        switch(result.score) {
            case 0:
                level = "Très faible";
                result_style = "bg-red-400";
                break;
            case 1:
                level = "Faible";
                result_style = "bg-orange-400";
                break;
            case 2:
            case 3:
                level = "Moyen";
                result_style = "bg-yellow-400";
                break;
            case 4:
                level = "Très fort";
                result_style = "bg-green-400";
                break;
        }
        uppercase = /[A-Z]/.test(password);
        number = /[0-9]/.test(password);
        special = /[!@#$%^&*(),.?":{}|<>;%£µ¨§/]/.test(password);
    }
</script>

<div class="relative z-10 container w-full p-9 flex flex-col gap-12">
    <div class="test_bloc w-full flex flex-col gap-3">
        <div class="title w-full flex items-center gap-2">
            <iconify-icon class="me-2 text-3xl" icon="mingcute:safe-lock-fill"></iconify-icon>
            <h1 class="text-xl font-semibold">Test de mot de passe</h1>
        </div>
        <Separator color='accent' width='w-2/5'/>
        <input
            class="mt-3 py-3 px-6 bg-gray-100 rounded-full w-full text-base outline-none"
            type="password"
            autocomplete="off"
            bind:value={password}
            placeholder="Tapez votre mot de passe..."
            on:input={checkPassword}
        />
        <div class="tags flex gap-3 mt-3 items-center gap-3">
            <div class="level py-2 px-4 {result_style}">
                <p class="text-white">{level}</p>
            </div>
            <div class="upper py-2 px-4 bg-gray-100">
                <p class="{uppercase ? 'text-green-400' : 'text-red-400'}">Majuscule</p>
            </div>
            <div class="number py-2 px-4 bg-gray-100">
                <p class="{number ? 'text-green-400' : 'text-red-400'}">Chiffre</p>
            </div>
            <div class="special py-2 px-4 bg-gray-100">
                <p class="{special ? 'text-green-400' : 'text-red-400'}">Caractère spécial</p>
            </div>
        </div>
        <div class="time_crack flex gap-3 items-center px-4 py-2 bg-gray-100">
            <p class="font-semibold">Temps pour cracker le mot de passe : </p>
            <p class="py-2 px-4 {result_style} text-white">{time}</p>
        </div>
    </div>

    <div class="tips_bloc w-full flex flex-col gap-3">
        <div class="title w-full flex items-center gap-2">
            <iconify-icon class="me-2 text-3xl" icon="mingcute:light-fill"></iconify-icon>
            <h1 class="text-xl font-semibold">Conseils</h1>
        </div>
        <Separator color='accent' width='w-2/5'/>
        <div class="tips mt-3">
            <ul>
                <li>Utilisez des majuscules et des minuscules</li>
                <li>Utilisez des chiffres</li>
                <li>Utilisez des caractères spéciaux</li>
                <li>Utilisez des mots de passe longs</li>
                <li>Ne réutilisez pas le même mot de passe</li>
            </ul>
        </div>
    </div>

</div>