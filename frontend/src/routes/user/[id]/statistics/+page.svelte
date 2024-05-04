<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import { popup, type PopupSettings } from "@skeletonlabs/skeleton";
    import DatePicker from "$lib/components/DatePicker/DatePicker.svelte";

    export let data;

    let count: number = 10;


    const datePopup: PopupSettings = {
        event: 'click',
        target: 'datePopup',
        placement: 'bottom',
        closeQuery: '#will-close'
    };

</script>

<div class="flex-col justify-center p-0 m-0">
    Settings
    <p class="m-1">
    <input type="number" class="variant-filled-secondary rounded p-2" bind:value={count} max="50" min="10"/>
    <button class="btn variant-filled-secondary p-2" use:popup={datePopup}>Date selection</button>
    </p>
    <div class="flex gap-5 flex-wrap justify-between">
        <TopList emotes={data.topEmotes.slice(0, count)} isTop={true} {count}/>
        <TopList emotes={data.bottomEmotes.slice(0, count)} isTop={false} {count}/>
    </div>

</div>

<div class="" data-popup="datePopup">
    <div class="bg-tertiary-100-800-token rounded">
        <DatePicker/>
    </div>
    <div class="arrow bg-tertiary-100-800-token"/>
</div>
