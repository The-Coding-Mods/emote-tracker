<script lang="ts">
    import { page } from "$app/state";
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";
    import { Tooltip } from "@skeletonlabs/skeleton-svelte";
    import { toaster } from "$lib/stores/toaster";
    import { DateTime } from "luxon";
    import { dateTime } from "$lib/stores/time";
    import { invalidateAll } from "$app/navigation";
    import { Configuration, UserApi } from "$lib/api";
    import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";
    import { ChartNoAxesColumn, RefreshCw, Twitch } from '@lucide/svelte';

    let { data, children } = $props();

    let openState = $state(false);

    const userApi = new UserApi(new Configuration({ basePath: BACKEND_BASE_PATH }));

    const dayFormatter = new Intl.DateTimeFormat(undefined!, {
        day: "2-digit",
        month: "short",
        year: "numeric",
    });
    const hourFormatter = new Intl.DateTimeFormat(undefined!, {
        day: "2-digit",
        month: "short",
        year: "numeric",
        hour: "2-digit",
        minute: "2-digit",
    });

    function removeTrailingPath(url: string): string {
        const splitUrl = url.split("/");
        if (splitUrl[splitUrl.length - 1] !== data.user.id) {
            splitUrl.pop();
        }
        return splitUrl.join("/");
    }

    const units: Intl.RelativeTimeFormatUnit[] = ["year", "month", "week", "day", "hour", "minute", "second"];

    function timeAgo(date: DateTime, currentDate: DateTime) {
        const diff = date.diff(currentDate).shiftTo(...units);
        const unit = units.find((unit) => diff.get(unit) !== 0) || "second";

        const relativeFormatter = new Intl.RelativeTimeFormat(undefined!, {
            numeric: "auto",
        });
        return relativeFormatter.format(Math.trunc(diff.as(unit)), unit);
    }

    async function handleUpdateClick() {
        const { added, removed, renamed } = await userApi.updateEmotesForUser({ userId: data.user.id });
        toaster.success({
            description: `Added: ${added?.length}, Removed: ${removed?.length}, Renamed: ${renamed?.length}`,
        });
        await invalidateAll();
    }
</script>

<svelte:head>
    <title>{`Emote Tracker - ${capitalizeFirstLetter(data.user.name)}`}</title>
</svelte:head>

<aside class="w-auto flex-none overflow-x-hidden">
    <div class="bg-secondary-50-950 grid h-full w-22 grid-rows-[auto_1fr_auto] gap-0 border-r">
        <div class="box-border">
            <a href="{removeTrailingPath(page.url.toString())}/statistics" class="bg-primary-hover aspect-square">
                <div class=" bg-primary-hover flex aspect-square w-full flex-col items-stretch justify-center space-y-1 text-center">
                    <div class="flex items-center justify-center">
                        <ChartNoAxesColumn strokeWidth="5" color="#ffffff" />
                    </div>
                    <div class="text-xs font-bold text-white">
                        <span class="text-lg">Statistics</span>
                    </div>
                </div>
            </a>
        </div>
    </div>
</aside>
<div class="flex flex-1 flex-col overflow-x-hidden">
    <main class="flex-auto">
        <div class="mx-2 flex justify-center">
            <div class="w-[100%] max-w-[400px] md:max-w-[800px] xl:max-w-[1000px] 2xl:max-w-[1200px]">
                {@render children?.()}
            </div>
        </div>
    </main>
</div>

<aside class="w-auto flex-none overflow-x-hidden overflow-y-auto">
    <div class="card preset-filled-tertiary-200-800 m-2 hidden w-fit p-2 lg:block">
        <div class="card-header flex items-center p-2 text-xl">
            <img class="square mr-2 w-16 rounded-full" src={data.user.profilePicture} alt={data.user.name} />
            {capitalizeFirstLetter(data.user.name)}
            <a
                href="//twitch.tv/{data.user.name}"
                target="_blank"
                rel="noopener noreferrer"
                title="twitch.tv/{data.user.name}"
                class="mx-2 hover:text-[#6441a5]"
                aria-label="{data.user.name} twitch channel"
            >
                <Twitch strokeWidth="3" absoluteStrokeWidth={false}/>
            </a>
        </div>
        <hr class="divide-tertiary-100-900 border-t-2!" />

        <div class="card-body mx-2">
            <table class="w-full">
                <tbody>
                    <tr>
                        <td>Tracking since:</td>
                        <td class="text-right">{dayFormatter.format(data.user.registered)}</td>
                    </tr>
                    <tr>
                        <td>Last updated:</td>
                        <td class="flex text-right">
                            <Tooltip
                                open={openState}
                                onOpenChange={(e) => (openState = e.open)}
                                positioning={{ placement: "bottom" }}
                                arrow
                                arrowBackground="!bg-tertiary-100-900"
                            >
                                {#snippet trigger()}
                                    <div>
                                        {timeAgo(DateTime.fromJSDate(data.user.lastUpdated), $dateTime)}
                                    </div>
                                {/snippet}
                                {#snippet content()}
                                    <div class="card bg-tertiary-100-900 p-1">{hourFormatter.format(data.user.lastUpdated)}</div>
                                {/snippet}
                            </Tooltip>
                            <button
                                class="btn-icon-sm -my-0.5"
                                title="Update Emotes"
                                onclick={handleUpdateClick}
                                aria-label="Update Emotes for user">
                                <RefreshCw size="16"/>
                            </button>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</aside>
