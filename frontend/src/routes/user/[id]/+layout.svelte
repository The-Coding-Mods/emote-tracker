<script lang="ts">
	import { page } from "$app/stores";
	import { capitalizeFirstLetter } from "$lib/common/StringFormatting";
	import { type PopupSettings, Navigation } from "@skeletonlabs/skeleton-svelte";
	import { toaster} from "$lib/stores/toaster";
	import { DateTime } from "luxon";
	import { dateTime } from "$lib/stores/time";
	import { invalidateAll } from "$app/navigation";
	import { Configuration, UserApi } from "$lib/api";
	import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";

	let { data, children } = $props();
	const userApi = new UserApi(new Configuration({ basePath: BACKEND_BASE_PATH }));

	const popupHover: PopupSettings = {
		event: "hover",
		target: "popupHover",
		placement: "bottom",
	};

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
<AppShell>
	{#snippet sidebarLeft()}
		<Navigation background="bg-secondary-50-950" border="border-r">
			<!--<AppRailAnchor href="{removeTrailingPath($page.url.toString())}">
                <svelte:fragment slot="lead"><i class="fa-solid fa-user text-xl"/></svelte:fragment>
                <span class="text-lg">Overview</span>
            </AppRailAnchor>-->
			<AppRailAnchor href="{removeTrailingPath($page.url.toString())}/statistics">
				{#snippet lead()}
					<i class="fa-solid fa-chart-simple text-xl"></i>
				{/snippet}
				<span class="text-lg">Statistics</span>
			</AppRailAnchor>
			<!--<AppRailAnchor href="{removeTrailingPath($page.url.toString())}/emotes">
                <svelte:fragment slot="lead"><i class="fa-solid fa-icons text-xl"></i></svelte:fragment>
                <span class="text-lg">Emotes</span>
            </AppRailAnchor>-->
		</Navigation>
	{/snippet}
	<div class="mx-2 flex justify-center">
		<div class="w-full max-w-[400px] md:max-w-[800px] xl:max-w-[1000px] 2xl:max-w-[1200px]">
			{@render children?.()}
		</div>
	</div>
	{#snippet sidebarRight()}
		<div class="card bg-tertiary-200-800 mt-2 mr-2 hidden lg:block">
			<div class="card-header flex items-center py-2 text-xl">
				<img class="square mr-2 w-16 rounded-full" src={data.user.profilePicture} alt={data.user.name} />
				{capitalizeFirstLetter(data.user.name)}
				<a
					href="//twitch.tv/{data.user.name}"
					target="_blank"
					rel="noopener noreferrer"
					title="twitch.tv/{data.user.name}"
					class="mx-2 hover:text-[#6441a5]"><i class="fa-brands fa-twitch"></i></a
				>
			</div>
			<hr class="divide-tertiary-50-950 mx-0.5 border-t-2!" />

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
								<div use:popup={popupHover}>
									{timeAgo(DateTime.fromJSDate(data.user.lastUpdated), $dateTime)}
								</div>
								<div class="bg-tertiary-100-900 card p-1" data-popup="popupHover">
									{hourFormatter.format(data.user.lastUpdated)}
									<div class="arrow bg-tertiary-100-900"></div>
								</div>
								<button class="btn-icon-sm -mx-1 -my-2" title="Update Emotes" onclick={handleUpdateClick}
									><i class="fa-solid fa-rotate-right"></i>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	{/snippet}
</AppShell>
