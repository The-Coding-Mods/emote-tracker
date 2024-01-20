import { Configuration, UserApi } from "$lib/../api";
import type { PageServerLoad } from './$types';
import { BACKEND_URL } from "$lib/common/ApiHost";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));
export const load: PageServerLoad = async ({ params }) => {
    const topEmotes = await userApi.getTopEmotes({userId: params.id, count: 50});
    const bottomEmotes = await userApi.getBottomEmotes({userId: params.id, count: 50});
    return {topEmotes, bottomEmotes}
}
