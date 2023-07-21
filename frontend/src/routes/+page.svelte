<script lang="ts">
    import Header from "$lib/components/Header.svelte";
    import TopList from "$lib/components/TopList.svelte";
    import Usage from "$lib/components/Usage.svelte";
</script>

<Header/>
<main class="">
    <div class="flex justify-evenly">
        <TopList isTop={true}/>
        <TopList isTop={false}/>
    </div>
    <Usage/>
</main>

