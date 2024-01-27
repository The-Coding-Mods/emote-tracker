import { error } from "@sveltejs/kit";
import type { PageServerLoad } from "./$types";
import { BACKEND_URL } from "$lib/common/ApiHost";
import { Configuration, UserApi } from "$lib/api";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));

export const load: PageServerLoad = async ({ params }) => {
  try {
    const topEmotes = await userApi.getTopEmotes({
      userId: params.id,
      count: 50,
    });
    const bottomEmotes = await userApi.getBottomEmotes({
      userId: params.id,
      count: 50,
    });
    const noUsage = await userApi.getEmotesWitNoUsage({ userId: params.id });
    return { topEmotes, bottomEmotes, noUsage };
  } catch (exception) {
    return error(500, "Unknown error");
  }
};
