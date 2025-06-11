<script lang="ts">
	import type { EmoteCount as ApiEmote } from "$lib/api";
	import Emote from "$lib/components/Emote.svelte";
	import { truncate } from "$lib/common/StringFormatting";
	import Spinner from "$lib/components/Spinner.svelte";

	interface Props {
		isTop: boolean;
		count: number;
		emotePromise: Promise<ApiEmote[]>;
	}

	let { isTop, count = $bindable(), emotePromise }: Props = $props();
</script>

<div class="bg-secondary-50-900-token flex min-w-88 flex-col rounded-2xl border-2">
	{#await emotePromise}
		<div class="flex h-full items-center justify-center">
			<Spinner />
		</div>
	{:then emotes}
		<h1 class="flex justify-center p-1.5 text-3xl">
			{#if isTop}
				Top
			{:else}
				Bottom
			{/if}
			{count} emotes
		</h1>

		<table class="border-separate p-4">
			<tbody>
				<tr>
					<th colspan="3" class="">Emote</th>
					<th>Count</th>
				</tr>
				{#each emotes.slice(0, count) as emote, i (emote.id)}
					<tr class="">
						<td class="p-4">#{i + 1}</td>
						<td class="">{truncate(emote.name, 14)}</td>
						<td class="">
							<Emote {emote} size="2x" />
						</td>
						<td class="">{emote.count}</td>
					</tr>
				{/each}
			</tbody>
		</table>
	{/await}
</div>
