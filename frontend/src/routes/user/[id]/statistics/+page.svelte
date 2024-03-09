<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";

    export let data;

    let count: number = 10;


</script>

<div class="flex-col justify-center p-0 m-0">
    <div class="flex justify-center mt-4">
        <input type="number" class="variant-filled-secondary rounded p-2 " bind:value={count} max="50" min="10"/>
    </div>
    <div class="flex justify-between">
        <TopList emotes={data.topEmotes.slice(0, count)} isTop={true} {count}/>
        <TopList emotes={data.bottomEmotes.slice(0, count)} isTop={false} {count}/>
    </div>
</div>

