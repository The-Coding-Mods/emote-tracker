<script lang="ts">
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";

    export let data;

</script>
<div class="flex justify-center pt-2">
    <div class="grid grid-cols-3">
        {#each data.users as user}
            <a href="/users/{user.id}">
                <div class="card bg-secondary-100-800-token p-2 flex items-center m-2 text-xl">
                    <img class="rounded-full w-16 square mr-2" src="{user.profilePicture}" alt="{user.name}"/>
                    {capitalizeFirstLetter(user.name)}
                </div>
            </a>
        {/each}
    </div>
</div>
