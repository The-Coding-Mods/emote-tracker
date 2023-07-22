<script lang="ts">
    import type { Emote } from "../../api";

    export let emote: Emote;
    export let size: "2x" | "4x";

    let imageUrl: string = `https://cdn.7tv.app/emote/${emote.id}/${size}.webp`;
    let emoteUrl: string = `https://7tv.app/emotes/${emote.id}`;
</script>

<a href="{emoteUrl}" target="_blank" rel="noopener noreferrer"
   class="snap-start shrink-0 w-[25%] hover:brightness-125">
    <img class="mx-1 aspect-square object-contain"
         src="{imageUrl}"
         alt="Emote with the name {emote.name}" width="{size === '2x' ? '64' : '128'}"/>
</a>
