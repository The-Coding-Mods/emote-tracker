<script type="ts">
    import { time } from '../stores/time';
    import { LightSwitch } from '@skeletonlabs/skeleton';

    const formatter = new Intl.DateTimeFormat(
        undefined,
        {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        }
    );
</script>

<nav class="bg-secondary-100-800-token max-h-18 min-h-18 h-18 drop-shadow-2xl">
    <div class="grid grid-cols-3">

        <a href="/"><h1 class="text-4xl p-4 font-bold">Emote Tracker</h1></a>

        <div class="flex justify-center align-middle">
            <a href="/user"><h1 class="text-3xl p-4 uppercase">users</h1></a>
        </div>

        <div class="text-4xl p-4 flex justify-end align-middle">
            {formatter.format($time)}
            <div class="m-2">
                <LightSwitch/>
            </div>
        </div>

    </div>
</nav>
