<script type="ts">
    import {time} from '../stores/count';

    const formatter = new Intl.DateTimeFormat(
        undefined,
        {
            hour: '2-digit',
            minute: '2-digit',
            second: '2-digit'
        }
    );
</script>

<header class="bg-lime-600 flex justify-between mb-5">
    <h1 class="text-4xl p-4 justify-center">Emote Tracker</h1>
    <div class="text-4xl p-4 align-middle">
        {formatter.format($time)}
    </div>
</header>
