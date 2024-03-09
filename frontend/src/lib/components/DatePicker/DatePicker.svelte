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
<Calendar/>
<div class="btn-group variant-filled ">
    <button class="btn variant-filled{$prioritizeStart ? '-secondary' : '' }" on:click={() => $prioritizeStart = true}>
        From: {shortVersion($selectedDates.from)}</button>
    <button class="variant-filled{$prioritizeStart?'':'-secondary' }" on:click={() =>  $prioritizeStart = false}>
        To: {shortVersion($selectedDates.to)}</button>
</div>
