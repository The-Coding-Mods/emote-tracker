<script lang="ts">

    import { Shadow } from "svelte-loading-spinners";
    import { Configuration, UserApi } from "../../api";

    let elemMovies: HTMLDivElement;
    const userApi = new UserApi(new Configuration({basePath: "http://localhost:8080"}));
    let promise = userApi.getEmotesWitNoUsage({userId: "40646018"});

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
<section class="pt-5 space-y-8">
    {#await promise}
        <h1 class="pl-4 text-4xl font-bold">Emotes with 0 usage</h1>
        <div class="ml-5 mt-5">
            <Shadow size="2" unit="rem" color="rgb(101 163 13)"/>
        </div>
    {:then emotes}
        <h1 class="pl-4 text-4xl font-bold">Emotes with 0 usage ({emotes.length})</h1>
        <div class="pl-4 grid grid-cols-[auto_1fr_auto] gap-4 items-center max-w-4xl">
            <!-- Button: Left -->
            <button type="button" class="btn-icon variant-filled" on:click={multiColumnLeft}>
                <i class="fa-solid fa-arrow-left"></i>
            </button>
            <!-- Carousel -->
            <div bind:this={elemMovies}
                 class="snap-x snap-mandatory scroll-smooth flex gap-2 pb-2 overflow-x-auto">
                {#each emotes as emote}
                    <a href="https://7tv.app/emotes/{emote.id}" target="_blank" rel="noopener noreferrer"
                       class="snap-start shrink-0 w-[25%] hover:brightness-125">
                        <img class="mx-1 aspect-square object-contain" src="https://cdn.7tv.app/emote/{emote.id}/4x.webp"
                             alt="Emote with the name {emote.name}" width="128"/>
                    </a>
                {/each}
            </div>
            <!-- Button-Right -->
            <button type="button" class="btn-icon variant-filled" on:click={multiColumnRight}>
                <i class="fa-solid fa-arrow-right"></i>
            </button>
        </div>
    {/await}
</section>
