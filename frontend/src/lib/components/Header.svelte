<script type="ts">
    import { time } from '../stores/count';

    const formatter = new Intl.DateTimeFormat(
        undefined,
        {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        }
    );
</script>


<nav class="bg-lime-600 fixed top-0 w-[100%] max-h-20 min-h-20">
    <div class="flex ms-2 h-20">
        <a href="/"><h1 class="text-4xl p-4 font-bold">Emote Tracker</h1></a>
        <div class="flex">
            <a href="/users"><h1 class="text-2xl p-4 uppercase">Users</h1></a>
            <div class="text-4xl p-4">
                {formatter.format($time)}
            </div>
        </div>
    </div>
</nav>
