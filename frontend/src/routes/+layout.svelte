<script lang="ts">
	// Finally, your application's global stylesheet (sometimes labeled 'app.css')
	import "../app.css";
	import Header from "$lib/components/Header.svelte";
    import { Toaster } from '@skeletonlabs/skeleton-svelte';
    import { toaster } from '$lib/stores/toaster';
    import { arrow, autoUpdate, computePosition, flip, offset, shift } from "@floating-ui/dom";
	interface Props {
		children?: import("svelte").Snippet;
	}

	let { children }: Props = $props();

</script>

<Toaster {toaster}></Toaster>
<AppShell>
	{#snippet header()}
		<Header />
	{/snippet}

	{@render children?.()}
</AppShell>
