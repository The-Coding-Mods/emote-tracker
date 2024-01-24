import { Configuration, ResponseError, UserApi } from "$lib/../api";
import type { PageServerLoad } from "./$types";
import { BACKEND_URL } from "$lib/common/ApiHost";
import type { UserLoad } from "./types";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));

export const load: PageServerLoad = async ({ params }): Promise<UserLoad> => {
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
    return { error: false, user, topEmotes, bottomEmotes, noUsage };
  } catch (error) {
    if (error instanceof ResponseError) {
      return {
        error: true,
        statusCode: error.response.status,
        message: error.message,
      };
    }
    return { error: true, statusCode: 500, message: "Unknown error" };
  }
};
