<script lang="ts">
    import Header from "$lib/components/Header.svelte";
    import TopList from "$lib/components/TopList.svelte";
</script>

<Header/>
<main class="flex justify-center">
    <TopList isTop={true}/>
    <TopList isTop={false}/>
</main>

