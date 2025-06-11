<script lang="ts">
    import "../app.css";
    import Header from "$lib/components/Header.svelte";
    import { Toaster } from "@skeletonlabs/skeleton-svelte";
    import { toaster } from "$lib/stores/toaster";

    interface Props {
        children?: import("svelte").Snippet;
    }

    let {children}: Props = $props();
</script>

<Toaster {toaster}></Toaster>
<div class="grid grid-rows-[auto_1fr_auto]">
    <Header/>
    <div class="container mx-auto grid grid-cols-1 xl:grid-cols-[200px_minmax(0px,_1fr)_200px]">
        {@render children?.()}
    </div>
</div>
