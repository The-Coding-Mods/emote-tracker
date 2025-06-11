<script lang="ts">
    import { capitalizeFirstLetter } from "$lib/common/StringFormatting";

    let { data } = $props();
</script>

<div class="flex justify-center pt-2">
    <div class="grid grid-cols-3">
        {#each data.users as user}
            <a href="/users/{user.id}">
                <div class="card bg-secondary-100-900 m-2 flex items-center p-2 text-xl">
                    <img class="square mr-2 w-16 rounded-full" src={user.profilePicture} alt={user.name} />
                    {capitalizeFirstLetter(user.name)}
                </div>
            </a>
        {/each}
    </div>
</div>
