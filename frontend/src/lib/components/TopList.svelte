<script lang="ts">

    import type { Emote as ApiEmote } from "$lib/api";
    import Emote from "$lib/components/Emote.svelte";
    import { truncate } from "$lib/common/StringFormatting";
    import Spinner from "$lib/components/Spinner.svelte";

    export let isTop!: boolean;
    export let count!: number;
    export let emotePromise!: Promise<ApiEmote[]>;
</script>

<div class="flex flex-col min-w-[22rem] rounded-2xl bg-secondary-50-900-token border-2">
    {#await emotePromise}
        <div class="flex justify-center items-center h-full">
            <Spinner/>
        </div>
    {:then emotes}
        <h1 class="text-3xl flex justify-center p-1.5">
            {#if isTop}
                Top
            {:else}
                Bottom
            {/if}
            {count} emotes</h1>

        <table class="border-separate p-4">
            <tbody>
            <tr>
                <th colspan="3" class="">Emote</th>
                <th>Count</th>
            </tr>
            {#each emotes.slice(0, count) as emote, i (emote.id)}
                <tr class="">
                    <td class="p-4">#{i + 1}</td>
                    <td class="">{truncate(emote.name, 14)}</td>
                    <td class="">
                        <Emote emote={emote} size="2x"/>
                    </td>
                    <td class="">{emote.count}</td>
                </tr>
            {/each}
            </tbody>
        </table>
    {/await}
</div>
