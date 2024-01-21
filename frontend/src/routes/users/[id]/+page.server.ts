import { UserApi } from "$lib/../api";
import type { PageServerLoad } from './$types';

const userApi = new UserApi();
export const load: PageServerLoad = async ({ params }) => {
    const topEmotes = await userApi.getTopEmotes({userId: params.id, count: 50});
    const bottomEmotes = await userApi.getBottomEmotes({userId: params.id, count: 50});
    return {topEmotes, bottomEmotes}
}
