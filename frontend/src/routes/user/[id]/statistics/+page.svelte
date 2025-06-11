<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";

    let { data } = $props();

    let count: number = $state(10);

</script>

<div class="flex-col justify-center p-0 m-0">
    <div class="flex justify-center mt-4">
        <input type="number" class="variant-filled-secondary rounded p-2 " bind:value={count} max="50" min="10"/>
    </div>
    <div class="flex justify-between">
        <TopList emotePromise={data.topEmotes} isTop={true} bind:count={count}/>
        <TopList emotePromise={data.bottomEmotes} isTop={false} bind:count={count}/>
    </div>
</div>

