import { error } from "@sveltejs/kit";
import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";
import { Configuration, UserApi } from "$lib/api";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ params, fetch, setHeaders }) => {
    const userApi = new UserApi(
        new Configuration({
            basePath: BACKEND_BASE_PATH,
            fetchApi: fetch,
        }),
    );
    setHeaders({
        'X-Accel-Buffering': 'no'
    });
    try {
        const topEmotes = async () => userApi.getTopEmotes({
            userId: params.id,
            count: 50,
        });
        const bottomEmotes = async () => userApi.getBottomEmotes({
            userId: params.id,
            count: 50,
        });
        return { topEmotes: topEmotes(), bottomEmotes: bottomEmotes() };
    } catch (exception) {
        return error(500, "Unknown error");
    }
};
