<script lang="ts">
    import { months } from "$lib/service/DateUtil";
    import { prioritizeStart, selectedDates } from "$lib/stores/SelectedDates";
    import Calendar from "$lib/components/DatePicker/Calendar.svelte";

    function shortVersion(date: Date | undefined) {
        if (date === undefined) {
            return "Add date";
        }
        return `${months[date.getMonth()]} ${date.getDate()}`
    }
</script>
<div class="flex flex-col justify-center">
    <Calendar/>
    <div class="inline-flex flex-grow-0 overflow-hidden rounded">
        <a id="wont-close"
           class="w-full py-[9px] px-5 variant-filled{$prioritizeStart ? '-secondary' : '' } hover:cursor-pointer"
           on:click={() => $prioritizeStart = true}>
            <div class="text-xs">
                From
            </div>
            <div>
                {shortVersion($selectedDates.from)}
            </div>
        </a>
        <span class="divider-vertical"/>
        <a id="wont-close"
           class="w-full py-[9px] px-5 variant-filled{$prioritizeStart ? '' : '-secondary' } hover:cursor-pointer"
           on:click={() =>  $prioritizeStart = false}>
            <div class="text-xs">
                To
            </div>
            <div>
                {shortVersion($selectedDates.to)}
            </div>
        </a>
    </div>
</div>
