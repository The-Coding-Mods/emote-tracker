<script lang="ts">
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";

    export let data;
</script>

<svelte:head>
    <title>{`Emote Tracker - ${capitalizeFirstLetter(data.user.name)}`}</title>
</svelte:head>

<slot/>
