import { Configuration, UserApi } from "$lib/../api";
import type { PageServerLoad } from './$types';

const userApi = new UserApi(new Configuration({basePath: "http://localhost:8080"}));
export const load: PageServerLoad = async ({ params }) => {
    let topEmotes = await userApi.getTopEmotes({userId: "40646018", count: 50});
    let bottomEmotes = await userApi.getBottomEmotes({userId: "40646018", count: 50});
    return {topEmotes, bottomEmotes}
}
