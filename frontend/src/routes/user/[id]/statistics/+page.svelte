<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";

    let { data } = $props();

    let count: number = $state(10);
</script>

<div class="m-0 flex-col justify-center p-0">
    <div class="mt-4 flex justify-center">
        <input type="number" class="preset-filled-secondary-500 rounded-sm p-2" bind:value={count} max="50" min="10" />
    </div>
    <div class="flex justify-between">
        <TopList emotePromise={data.topEmotes} isTop={true} bind:count />
        <TopList emotePromise={data.bottomEmotes} isTop={false} bind:count />
    </div>
</div>
