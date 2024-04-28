<script lang="ts">
    import { page } from '$app/stores';
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";
    import {
        AppRail,
        AppRailAnchor,
        AppShell,
        getToastStore,
        popup,
        type PopupSettings,
        type ToastSettings
    } from "@skeletonlabs/skeleton";
    import { DateTime } from "luxon";
    import { dateTime } from "$lib/stores/time";
    import { invalidateAll } from "$app/navigation";
    import { Configuration, UserApi } from "$lib/api";
    import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";

    export let data;
    const userApi = new UserApi(new Configuration({basePath: BACKEND_BASE_PATH}))
    const toastStore = getToastStore();

    const popupHover: PopupSettings = {
        event: 'hover',
        target: 'popupHover',
        placement: 'bottom'
    };


    const dayFormatter = new Intl.DateTimeFormat(
        undefined!,
        {
            day: '2-digit',
            month: 'short',
            year: 'numeric'
        }
    );
    const hourFormatter = new Intl.DateTimeFormat(
        undefined!,
        {
            day: '2-digit',
            month: 'short',
            year: 'numeric',
            hour: '2-digit',
            minute: "2-digit"
        }
    )

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

    async function handleUpdateClick() {
        const {added, removed, renamed} = await userApi.updateEmotesForUser({userId: data.user.id});
        const t: ToastSettings = {
            message: `Added: ${added?.length}, Removed: ${removed?.length}, Renamed: ${renamed?.length}`,
        };
        toastStore.trigger(t);
        await invalidateAll();
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
                   title="twitch.tv/{data.user.name}"
                   class="mx-2 hover:text-[#6441a5]"><i class="fa-brands fa-twitch"/></a>
            </div>
            <hr class="!border-t-2 divide-tertiary-50-900-token mx-0.5"/>

            <div class="card-body mx-2">
                <table class="w-full">
                    <tr>
                        <td>Tracking since:</td>
                        <td class="text-right">{dayFormatter.format(data.user.registered)}</td>
                    </tr>
                    <tr>
                        <td>Last updated:</td>
                        <td class="text-right flex">
                            <div use:popup={popupHover}>
                                {timeAgo(DateTime.fromJSDate(data.user.lastUpdated), $dateTime)}
                            </div>
                            <div class="bg-tertiary-100-800-token card p-1"
                                 data-popup="popupHover">{hourFormatter.format(data.user.lastUpdated)}
                                <div class="arrow bg-tertiary-100-800-token"/>
                            </div>
                            <button class="btn-icon-sm mx-[-0.25rem] my-[-0.5rem]" title="Update Emotes"
                                    on:click={handleUpdateClick}><i class="fa-solid fa-rotate-right"></i>
                            </button>
                        </td>
                    </tr>
                </table>
            </div>
        </div>
    </svelte:fragment>
</AppShell>

