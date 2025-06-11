<script lang="ts">
    import { time } from "$lib/stores/time";
    import LightSwitch from "$lib/components/LightSwitch.svelte";

    const formatter = new Intl.DateTimeFormat(undefined, {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
    });
</script>
<header class="bg-secondary-100-900 drop-shadow-2xl overflow-hidden">
    <nav class="">
        <div class="h-18 max-h-18 min-h-18  grid grid-cols-3 items-center">
            <a href="/"><h1 class="text-4xl font-bold">Emote Tracker</h1></a>

            <div class="flex justify-center">
                <a href="/user"><h1 class="text-3xl uppercase">users</h1></a>
            </div>

            <div class="flex justify-end text-4xl">
                {formatter.format($time)}
                <div class="flex justify-center m-2">
                    <LightSwitch/>
                </div>
            </div>
        </div>
    </nav>
</header>