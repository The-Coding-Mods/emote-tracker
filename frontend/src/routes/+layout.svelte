<script lang="ts">
    import "../app.css";
    import Header from "$lib/components/Header.svelte";
    import { Toaster } from "@skeletonlabs/skeleton-svelte";
    import { toaster } from "$lib/stores/toaster";

    interface Props {
        children?: import("svelte").Snippet;
    }

    let { children }: Props = $props();
</script>

<Toaster {toaster}></Toaster>
<div class="flex h-full w-full flex-col overflow-hidden">
    <Header />
    <div class="flex h-full w-full flex-auto overflow-hidden">
        {@render children?.()}
    </div>
</div>
