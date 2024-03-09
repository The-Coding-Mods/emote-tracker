<script lang="ts">
    import { prioritizeStart, type Range, selectedDates } from "$lib/stores/SelectedDates";
    import { days, months, isoDateString } from "$lib/service/DateUtil";

    type Week = {
        days: Day[];
    }
    type Day = {
        value: Date;
        selected: string;
        outside: string;
    }

    let value: Date = new Date();
    export let date = isoDateString(value);
    $: date = isoDateString(value);


    $: weeks = weeksFrom(value, $selectedDates);

    function go(direction: number) {
        value = new Date(value.getFullYear(), value.getMonth() + direction, value.getDate());
    }

    function handleStartDate(day: Day) {
        if ($selectedDates.to && day.value > new Date($selectedDates.to)) {
            $selectedDates.from = day.value;
            $selectedDates.to = undefined;
            return;
        }
        $selectedDates.from = day.value;
        return;
    }

    function setValue(day: Day) {
        if (!$selectedDates.from || $prioritizeStart) {
            handleStartDate(day);
            $prioritizeStart = false;
            return
        }
        if (day.value < new Date($selectedDates.from)) {
            $selectedDates.from = day.value;
            $selectedDates.to = undefined;
        } else {
            $selectedDates.to = day.value
        }
    }

    function weeksFrom(currentDate: Date, selected: Range<Date | undefined>) {
        const firstDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);
        // that's some magic copied from stackoverflow
        const offsetToPreviousMonday = firstDayOfMonth.getDate() + ((1 - firstDayOfMonth.getDay() - 7) % 7);
        const firstDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), offsetToPreviousMonday);

        const lastDayOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);
        const offsetToNextSunday = lastDayOfMonth.getDate() + ((1 - lastDayOfMonth.getDay() + 6) % 7);
        const lastDay = new Date(currentDate.getFullYear(), currentDate.getMonth(), offsetToNextSunday);

        let dayIterator = new Date(firstDay.getTime());
        const weeks: Week[] = [];

        let week: Day[] = [];
        while (dayIterator.getTime() <= lastDay.getTime()) {
            week.push({
                value: dayIterator,
                selected: isoDateString(selected.from) === isoDateString(dayIterator) || isoDateString(selected.to) === isoDateString(dayIterator) ? 'bg-primary-500 rounded-full font-bold' : '',
                outside: dayIterator.getMonth() !== currentDate.getMonth() ? 'opacity-25' : '',
            });

            dayIterator = new Date(dayIterator.getFullYear(), dayIterator.getMonth(), dayIterator.getDate() + 1);

            if (dayIterator.getDay() === 1) {
                weeks.push({days: week});
                week = [];
            }
        }

        return weeks;
    }


</script>

<div>
    <table class="text-center m-0 p-0">
        <tr class="">
            <td class="w-[32px] h-[32px] btn-icon variant-ghost-primary hover:cursor-pointer" on:click={() => go(-1)}><i
                    class="fa-solid fa-arrow-left"></i></td>
            <td colspan=5>{months[value.getMonth()]} {value.getFullYear()}</td>
            <td class="w-[32px] h-[32px] btn-icon variant-ghost-primary hover:cursor-pointer" on:click={() => go(+1)}><i
                    class="fa-solid fa-arrow-right"></i></td>
        </tr>
        <tr>
            {#each days as day}
                <th class="w-[32px] h-[32px] font-bold">{day}</th>
            {/each}
        </tr>
        {#each weeks as week}
            <tr>
                {#each week.days as day}
                    <td class="w-[32px] h-[32px] {day.outside} {day.selected} hover:bg-tertiary-800 hover:cursor-pointer hover:rounded-full"
                        on:click={() => setValue(day)}>{day.value.getDate()}</td>
                {/each}
            </tr>
        {/each}
    </table>
</div>

