<script lang="ts">
    import { time } from "$lib/stores/time";
    import LightSwitch from "$lib/components/LightSwitch.svelte";

    const formatter = new Intl.DateTimeFormat(undefined, {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
</script>

<header class="bg-secondary-100-900 overflow-hidden drop-shadow-2xl">
    <nav class="grid h-18 max-h-18 min-h-18 grid-cols-3 items-center px-2">
        <a href="/"><h1 class="text-4xl font-bold">Emote Tracker</h1></a>
        <div class="flex justify-center">
            <a href="/user"><h1 class="text-3xl uppercase">users</h1></a>
        </div>
        <div class="flex justify-end text-4xl">
            {formatter.format($time)}
            <div class="m-2 flex justify-center">
                <LightSwitch />
            </div>
        </div>
    </nav>
</header>
