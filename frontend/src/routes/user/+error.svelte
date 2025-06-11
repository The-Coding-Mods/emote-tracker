<script lang="ts">
	import { page } from '$app/state';
</script>

{#if page.status === 404}
	<div class="flex justify-center">
		<p class="text-2xl">User not found</p>
	</div>
{:else}
	<div class="flex justify-center">
		<p class="text-2xl">Unknown error</p>
	</div>
{/if}
