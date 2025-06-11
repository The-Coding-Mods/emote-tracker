<script lang="ts">
    import { Switch } from "@skeletonlabs/skeleton-svelte";
    import { Moon, Sun } from '@lucide/svelte';
    let checked = $state(true);

    $effect(() => {
        const mode = localStorage.getItem("mode") || "dark";
        checked = mode === "light";
    });

    const onCheckedChange = (event: { checked: boolean }) => {
        const mode = event.checked ? "light" : "dark";
        document.documentElement.setAttribute("data-mode", mode);
        localStorage.setItem("mode", mode);
        checked = event.checked;
    };
</script>

<svelte:head>
    <script>
        const mode = localStorage.getItem("mode") || "dark";
        document.documentElement.setAttribute("data-mode", mode);
    </script>
</svelte:head>

<Switch {checked} {onCheckedChange}>
    {#snippet inactiveChild()}
        <Moon size="14" />
    {/snippet}
    {#snippet activeChild()}
        <Sun size="14" />
    {/snippet}
</Switch>
