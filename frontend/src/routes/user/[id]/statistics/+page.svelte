<script lang="ts">
    import { getToastStore, type ToastSettings } from '@skeletonlabs/skeleton';
    import TopList from "$lib/components/TopList.svelte";
    import { Configuration, UserApi } from "$lib/api";
    import { BACKEND_URL } from "$lib/common/ApiHost";
    import { invalidateAll } from '$app/navigation';
    export let data;
    const toastStore = getToastStore();

    let count: number = 10;
    const userApi = new UserApi(new Configuration({basePath: BACKEND_URL}))

    async function handleUpdateClick() {
        const {added, removed, renamed} = await userApi.updateEmotesForUser({userId: data.user.id});
        const t: ToastSettings = {
            message: `Added: ${added?.length}, Removed: ${removed?.length}, Renamed: ${renamed?.length}`,
        };
        toastStore.trigger(t);
        invalidateAll();
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
        <div class="flex items-center">
            <button class="btn-md variant-filled bg-secondary-50-900-token rounded border-2 text-primary-900-50-token"
                    on:click={handleUpdateClick}>Update
            </button>
        </div>
    </div>
</div>

