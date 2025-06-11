<script lang="ts">
	import type { Emote } from "$lib/api";

	interface Props {
		emote: Emote;
		size: "2x" | "4x";
	}

	let { emote, size }: Props = $props();

	let imageUrl: string = `https://cdn.7tv.app/emote/${emote.id}/${size}.webp`;
	let emoteUrl: string = `https://7tv.app/emotes/${emote.id}`;
</script>

<a href={emoteUrl} target="_blank" rel="noopener noreferrer" class="w-[25%] shrink-0 snap-start hover:brightness-125">
	<img
		class="mx-1 aspect-square object-contain"
		src={imageUrl}
		loading="lazy"
		alt="Emote with name {emote.name}"
		width={size === "2x" ? "64" : "128"}
	/>
</a>
