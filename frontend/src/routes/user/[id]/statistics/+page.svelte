<script lang="ts">
    import TopList from "$lib/components/TopList.svelte";
    import { Accordion, AccordionItem, popup, type PopupSettings } from "@skeletonlabs/skeleton";
    import DatePicker from "$lib/components/DatePicker/DatePicker.svelte";
    import { prioritizeStart, selectedDates } from "$lib/stores/SelectedDates";
    import { shortVersion } from "$lib/service/DateUtil.js";

    export let data;


    enum TimeFrame {
        ALL = 'All',
        LAST_WEEK = 'Last week',
        LAST_MONTH = 'Last month',
        LAST_UPDATE = 'Last update',
        CUSTOM = 'Custom'
    }

    let count: number = 10;

    let selectedTimeFrame = TimeFrame.ALL;

    const datePopup: PopupSettings = {
        event: 'click',
        target: 'datePopup',
        placement: 'bottom',
        closeQuery: '#will-close'
    };
</script>


<Accordion width="w-[45%]">
    <h1 class="text-2xl">Settings</h1>
    <AccordionItem>
        <svelte:fragment slot="summary">Number of emotes to display</svelte:fragment>
        <svelte:fragment slot="content">
            <input type="number" class="variant-filled-secondary rounded p-2 m-2" bind:value={count} max="50" min="10"/>
        </svelte:fragment>
    </AccordionItem>
    <AccordionItem open>
        <svelte:fragment slot="summary">Time frame</svelte:fragment>
        <svelte:fragment slot="content">
            <div class="grid grid-cols-date gap-1">
                <button class="btn btn-sm variant-filled-{selectedTimeFrame === TimeFrame.ALL ? 'tertiary' : 'secondary'}"
                        on:click={() => selectedTimeFrame = TimeFrame.ALL}>All
                </button>
                <button class="btn btn-sm variant-filled-{selectedTimeFrame === TimeFrame.LAST_WEEK ? 'tertiary' : 'secondary'}"
                        on:click={() => selectedTimeFrame = TimeFrame.LAST_WEEK}>Last week
                </button>
                <button class="btn btn-sm variant-filled-{selectedTimeFrame === TimeFrame.LAST_MONTH ? 'tertiary' : 'secondary'}"
                        on:click={() => selectedTimeFrame = TimeFrame.LAST_MONTH}>Last month
                </button>
                <button class="btn btn-sm variant-filled-{selectedTimeFrame === TimeFrame.LAST_UPDATE ? 'tertiary' : 'secondary'}"
                        on:click={() => selectedTimeFrame = TimeFrame.LAST_UPDATE}>Since last update
                </button>
                <button class="btn btn-sm variant-filled-{selectedTimeFrame === TimeFrame.CUSTOM ? 'tertiary' : 'secondary'}"
                        use:popup={datePopup} on:click={() =>  {selectedTimeFrame = TimeFrame.CUSTOM; $prioritizeStart = true}}>
                    {shortVersion($selectedDates.from, "select date")} - {shortVersion($selectedDates.to, "select date")}
                </button>
            </div>
        </svelte:fragment>
    </AccordionItem>
</Accordion>

<div class="flex-col justify-center p-0 m-0">
    <div class="flex gap-5 flex-wrap justify-between">
        <TopList emotes={data.topEmotes.slice(0, count)} isTop={true} {count}/>
        <TopList emotes={data.bottomEmotes.slice(0, count)} isTop={false} {count}/>
    </div>
</div>

<div class="" data-popup="datePopup">
    <div class="bg-tertiary-100-800-token rounded">
        <DatePicker showDates={false}/>
    </div>
    <div class="arrow bg-tertiary-100-800-token"/>
</div>
