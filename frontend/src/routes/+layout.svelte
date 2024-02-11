<script lang="ts">
    // Finally, your application's global stylesheet (sometimes labeled 'app.css')
    import '../app.css';
    import Header from "$lib/components/Header.svelte";
    import { AppShell, initializeStores, Toast } from "@skeletonlabs/skeleton";


    initializeStores();
</script>
<Toast/>
<AppShell>
    <svelte:fragment slot="header">
        <Header/>
    </svelte:fragment>

    <slot/>
</AppShell>
