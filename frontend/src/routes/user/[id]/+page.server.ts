import { Configuration, UserApi } from "$lib/../api";
import type { PageServerLoad } from "./$types";
import { BACKEND_URL } from "$lib/common/ApiHost";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));
export const load: PageServerLoad = async ({ params }) => {
    try {
        const user = await userApi.getUser({ userId: params.id });
        const topEmotes = await userApi.getTopEmotes({
          userId: params.id,
          count: 50,
        });
        const bottomEmotes = await userApi.getBottomEmotes({
          userId: params.id,
          count: 50,
        });
        const noUsage = await userApi.getEmotesWitNoUsage({ userId: params.id });
        return { user, topEmotes, bottomEmotes, noUsage };
      } catch (error) {
        return {};
      }
};
