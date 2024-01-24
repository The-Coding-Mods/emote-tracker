<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import Usage from "$lib/components/Usage.svelte";
    import { Configuration, UserApi } from "$lib/../api";
    import { BACKEND_URL } from "$lib/common/ApiHost";
    import type { UserLoad } from "./types";

    export let data: UserLoad;
    let count: number = 10;
    const userApi = new UserApi(new Configuration({basePath: BACKEND_URL}))

    async function handleUpdateClick() {
        if (data.error) return;
        await userApi.updateEmotesForUser({userId: data.user.id});
    }

    function capitalizeFirstLetter(string: string) {
        return string.charAt(0).toUpperCase() + string.slice(1);
    }
</script>

<svelte:head>
    <title>{!data.error ? `Emote Tracker - ${capitalizeFirstLetter(data.user.name)}` : 'Emote Tracker'}</title>
</svelte:head>
<div class="relative w-[100%] max-w-[1400px] mr-auto ml-auto pr-3 pl-3">
<div class="flex-col justify-center p-0 m-0">
    {#if !data.error}
        <div class="flex justify-center mt-4">
            <input type="number" class="variant-filled-secondary rounded p-2 " bind:value={count} max="50" min="10"/>
        </div>
        <div class="flex justify-between">
            <TopList emotes={data.topEmotes.slice(0, count)} isTop={true} {count}/>
            <TopList emotes={data.bottomEmotes.slice(0, count)} isTop={false} {count}/>
        </div>
        <div class="flex justify-between mt-4">
            {#if data.noUsage.length > 0 }
                <Usage emotes={data.noUsage}/>
            {/if}
            <div>
                <button class="btn-md variant-filled bg-secondary-50-900-token rounded border-2 text-primary-900-50-token"
                        on:click={handleUpdateClick}>Update
                </button>
            </div>
        </div>

    {:else }
        <div class="flex justify-center">
            <p class="text-2xl">User not found</p>
        </div>
    {/if}
</div>
</div>
