<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import Usage from "$lib/components/Usage.svelte";
    import { Configuration, UserApi } from "$lib/api";
    import { BACKEND_URL } from "$lib/common/ApiHost";

    export let data;

    let count: number = 10;
    const userApi = new UserApi(new Configuration({basePath: BACKEND_URL}))

    async function handleUpdateClick() {
        await userApi.updateEmotesForUser({userId: data.user.id});
    }

</script>

<div class="flex-col justify-center p-0 m-0">
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
        <div class="flex items-center">
            <button class="btn-md variant-filled bg-secondary-50-900-token rounded border-2 text-primary-900-50-token"
                    on:click={handleUpdateClick}>Update
            </button>
        </div>
    </div>
</div>

