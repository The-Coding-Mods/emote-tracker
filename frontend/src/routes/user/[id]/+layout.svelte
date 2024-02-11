<script lang="ts">
    import { page } from '$app/stores';
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";
    import { AppRail, AppRailAnchor, AppShell } from "@skeletonlabs/skeleton";
    import { DateTime } from "luxon";
    import { dateTime } from "$lib/stores/time";

    export let data;

    const formatter = new Intl.DateTimeFormat(
        undefined!,
        {
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        }
    );

    function removeTrailingPath(url: string): string {
        const splitUrl = url.split("/");
        if (splitUrl[splitUrl.length - 1] !== data.user.id) {
            splitUrl.pop();
        }
        return splitUrl.join("/");
    }

    const units: Intl.RelativeTimeFormatUnit[] = [
        'year',
        'month',
        'week',
        'day',
        'hour',
        'minute',
        'second',
    ];

    function timeAgo(date: DateTime, currentDate: DateTime) {
        const diff = date.diff(currentDate).shiftTo(...units);
        const unit = units.find((unit) => diff.get(unit) !== 0) || 'second';

        const relativeFormatter = new Intl.RelativeTimeFormat(undefined!, {
            numeric: 'auto',
        });
        return relativeFormatter.format(Math.trunc(diff.as(unit)), unit);
    }

</script>

<svelte:head>
    <title>{`Emote Tracker - ${capitalizeFirstLetter(data.user.name)}`}</title>
</svelte:head>
<AppShell>
    <svelte:fragment slot="sidebarLeft">
        <AppRail background="bg-secondary-50-900-token" border="border-r">
            <!--<AppRailAnchor href="{removeTrailingPath($page.url.toString())}">
                <svelte:fragment slot="lead"><i class="fa-solid fa-user text-xl"/></svelte:fragment>
                <span class="text-lg">Overview</span>
            </AppRailAnchor>-->
            <AppRailAnchor href="{removeTrailingPath($page.url.toString())}/statistics">
                <svelte:fragment slot="lead"><i class="fa-solid fa-chart-simple text-xl"></i></svelte:fragment>
                <span class="text-lg">Statistics</span>
            </AppRailAnchor>
            <!--<AppRailAnchor href="{removeTrailingPath($page.url.toString())}/emotes">
                <svelte:fragment slot="lead"><i class="fa-solid fa-icons text-xl"></i></svelte:fragment>
                <span class="text-lg">Emotes</span>
            </AppRailAnchor>-->
        </AppRail>
    </svelte:fragment>
    <div class="flex justify-center mx-2">
        <div class="w-[100%] max-w-[400px] 2xl:max-w-[1200px] xl:max-w-[1000px] md:max-w-[800px]">
            <slot/>
        </div>
    </div>
    <svelte:fragment slot="sidebarRight">
        <div class="card hidden lg:block bg-tertiary-200-700-token mt-2 mr-2">
            <div class="card-header flex items-center py-2 text-xl">
                <img class="rounded-full w-16 square mr-2" src="{data.user.profilePicture}" alt="{data.user.name}"/>
                {capitalizeFirstLetter(data.user.name)}
                <a href="//twitch.tv/{data.user.name}" target="_blank" rel="noopener noreferrer"
                   class="mx-2 hover:text-[#6441a5]"><i class="fa-brands fa-twitch"/></a>
            </div>
            <hr class="!border-t-2 divide-tertiary-50-900-token mx-0.5"/>

            <div class="card-body mx-2">
                <table class="w-full">
                    <tr>
                        <td>Tracking since:</td>
                        <td class="text-right">{formatter.format(data.user.registered)}</td>
                    </tr>
                    <tr>
                        <td>Last updated:</td>
                        <td class="text-right">{timeAgo(DateTime.fromJSDate(data.user.lastUpdated), $dateTime)}</td>
                    </tr>
                </table>
            </div>
        </div>
    </svelte:fragment>
</AppShell>

