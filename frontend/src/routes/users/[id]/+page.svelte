<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import Usage from "$lib/components/Usage.svelte";
    import type { PageData } from './$types';

    export let data: PageData;
    let count: number = 10;
</script>

<div class="flex justify-center mt-4">
    <input type="number" class="variant-filled-secondary rounded p-2 " bind:value={count} max="50" min="10"/>

</div>
<div class="flex justify-evenly">
    <TopList emotes={data.topEmotes.slice(0,count)} isTop={true} {count}/>
    <TopList emotes={data.bottomEmotes.slice(0,count)} isTop={false} {count}/>
</div>
<Usage/>
