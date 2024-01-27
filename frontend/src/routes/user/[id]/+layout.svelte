<script lang="ts">
    import { page } from '$app/stores';
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";
    import { AppRail, AppRailAnchor, AppShell } from "@skeletonlabs/skeleton";

    export let data;

    function removeTrailingPath(url: string): string {
        const splitUrl = url.split("/");
        if (splitUrl[splitUrl.length - 1] !== data.user.id) {
            splitUrl.pop();
        }
        return splitUrl.join("/");
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
    <slot/>
</AppShell>

