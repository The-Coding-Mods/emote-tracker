<script lang="ts">
    // Finally, your application's global stylesheet (sometimes labeled 'app.css')
    import '../app.css';
    import Header from "$lib/components/Header.svelte";
    import { AppShell } from "@skeletonlabs/skeleton";
</script>
<AppShell>
    <svelte:fragment slot="header">
        <Header/>
    </svelte:fragment>

    <slot/>
</AppShell>
