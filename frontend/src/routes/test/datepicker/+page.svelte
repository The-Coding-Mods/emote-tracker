<script lang="ts">
    import DatePicker from "$lib/components/DatePicker.svelte";
    import { months } from "$lib/service/DateUtil";
    import { prioritizeStart, selectedDates } from "$lib/stores/SelectedDates";

    function shortVersion(date: Date | undefined) {
        if (date === undefined) {
            return "Add date";
        }
        return `${months[date.getMonth()]} ${date.getDate()}`
    }
</script>
<DatePicker/>
<div class="btn-group variant-filled ">
    <button class="btn variant-filled{$prioritizeStart ? '-secondary' : '' }" on:click={() => $prioritizeStart = true}>
        Min: {shortVersion($selectedDates.from)}</button>
    <button class="variant-filled{$prioritizeStart?'':'-secondary' }" on:click={() =>  $prioritizeStart = false}>
        Max: {shortVersion($selectedDates.to)}</button>
</div>
