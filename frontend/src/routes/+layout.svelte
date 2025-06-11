<script lang="ts">
	// Finally, your application's global stylesheet (sometimes labeled 'app.css')
	import "../app.css";
	import Header from "$lib/components/Header.svelte";
	import { AppShell, initializeStores, storePopup, Toast } from "@skeletonlabs/skeleton";
	import { arrow, autoUpdate, computePosition, flip, offset, shift } from "@floating-ui/dom";
	interface Props {
		children?: import("svelte").Snippet;
	}

	let { children }: Props = $props();

	storePopup.set({ computePosition, autoUpdate, offset, shift, flip, arrow });

	initializeStores();
</script>

<Toast />
<AppShell>
	{#snippet header()}
		<Header />
	{/snippet}

	{@render children?.()}
</AppShell>
