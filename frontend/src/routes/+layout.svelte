<script lang="ts">

    // Skeleton theme:
    import '@skeletonlabs/skeleton/themes/theme-skeleton.css';

    // This contains the bulk of Skeletons required styles:
    import '@skeletonlabs/skeleton/styles/skeleton.css';

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
