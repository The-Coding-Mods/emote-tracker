<script lang="ts">

    import { Shadow } from "svelte-loading-spinners";
    import { Configuration, UserApi } from "../../api";

    function truncate(input: string, length: number) {
        return input.length > length
            ? `${input.substring(0, length)}...`
            : input;
    }

    const userApi = new UserApi(new Configuration({basePath: "http://localhost:8080"}));
    export let isTop!: boolean;
    export let count!: number;
    let promise = isTop ? userApi.getTopEmotes({userId: "40646018", count}) : userApi.getBottomEmotes({userId: "40646018", count});
</script>

<div class="flex flex-col mx-2 min-w-[22rem] rounded-2xl bg-zinc-700 drop-shadow-lg">
    <h1 class="text-3xl flex justify-center p-1.5">
        {#if isTop}
            Top
        {:else}
            Bottom
        {/if}
        5 emotes</h1>
    {#await promise}
        <div class="flex justify-center my-5">
            <Shadow size="2" unit="rem" color="rgb(101 163 13)"/>
        </div>
    {:then emotes}
            <table class="border-separate p-4">
                <thead>
                <th colspan="3" class="">Emote</th>
                <th>Count</th>
                </thead>
                <tbody>
                {#each emotes as emote, i (emote.id)}
                    <tr class="">
                        <td class="p-4">#{i + 1}</td>
                        <td class="">{truncate(emote.name, 14)}</td>
                        <td class="h-[65px]">
                            <a href="https://7tv.app/emotes/{emote.id}" target="_blank" rel="noopener noreferrer">
                                <img class="mx-1" src="https://cdn.7tv.app/emote/{emote.id}/2x.webp" width="64" height="64" loading="lazy"
                                     alt="Emote with the name {emote.name}"/>
                            </a>
                        </td>
                        <td class="">{emote.count}</td>
                    </tr>
                {/each}
                </tbody>
            </table>
    {/await}
</div>
