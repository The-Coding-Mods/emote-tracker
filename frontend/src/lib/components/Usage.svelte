<script lang="ts">

    import type { Emote as ApiEmote } from "$lib/api";
    import Emote from "$lib/components/Emote.svelte";

    let elemMovies: HTMLDivElement;
    export let emotes!: ApiEmote[];

    function multiColumnLeft(): void {
        let x = elemMovies.scrollWidth;
        if (elemMovies.scrollLeft !== 0) x = elemMovies.scrollLeft - elemMovies.clientWidth;
        elemMovies.scroll(x, 0);
    }

    function multiColumnRight(): void {
        let x = 0;
        // -1 is used because different browsers use different methods to round scrollWidth pixels.
        if (elemMovies.scrollLeft < elemMovies.scrollWidth - elemMovies.clientWidth - 1) x = elemMovies.scrollLeft + elemMovies.clientWidth;
        elemMovies.scroll(x, 0);
    }

</script>
<section class="space-y-8">
    <h1 class="pl-4 text-4xl font-bold">Emotes with 0 usage ({emotes.length})</h1>
    <div class="pl-4 grid grid-cols-[auto_1fr_auto] gap-4 items-center max-w-4xl">
        <!-- Button: Left -->
        {#if emotes.length > 4}
            <button type="button" class="btn-icon variant-filled" on:click={multiColumnLeft}>
                <i class="fa-solid fa-arrow-left"></i>
            </button>
        {/if}
        <!-- Carousel -->
        <div bind:this={elemMovies} class="snap-x snap-mandatory scroll-smooth flex gap-2 pb-2 overflow-x-auto">
            {#each emotes as emote}
                <Emote emote={emote} size="4x"/>
            {/each}
        </div>
        <!-- Button-Right -->
        {#if emotes.length > 4}
            <button type="button" class="btn-icon variant-filled" on:click={multiColumnRight}>
                <i class="fa-solid fa-arrow-right"></i>
            </button>
        {/if}
    </div>
</section>
