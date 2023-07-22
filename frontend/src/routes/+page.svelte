<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import Usage from "$lib/components/Usage.svelte";
</script>

<main class="">
    <div class="flex justify-evenly">
        <TopList isTop={true} count={10}/>
        <TopList isTop={false} count={10}/>
    </div>
    <Usage/>
</main>

