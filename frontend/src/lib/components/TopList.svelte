<script lang="ts">

    import {Shadow} from "svelte-loading-spinners";

    export let isTop: boolean;
    let promise = new Promise(() => true);
</script>

<div class="flex flex-col mx-2 min-w-[20rem] rounded-2xl bg-zinc-700 drop-shadow-lg">
    <h1 class="text-3xl flex justify-center p-1.5">
        {#if isTop}
            Top
        {:else}
            Bottom
        {/if}
        5 emotes</h1>
    {#await promise}
        <div class="flex justify-center my-5">
            <Shadow size="2" unit="rem" color="rgb(101 163 13)"/>
        </div>
    {/await}
</div>
