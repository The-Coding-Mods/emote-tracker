import { BACKEND_URL } from "$lib/common/ApiHost";
import { Configuration, ResponseError, UserApi } from "$lib/api";
import type { LayoutServerLoad } from "./$types";
import { error } from "@sveltejs/kit";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));
export const load: LayoutServerLoad = async ({ params }) => {
  try {
    const user = await userApi.getUser({ userId: params.id });
    return { user: user };
  } catch (exception) {
    if (exception instanceof ResponseError) {
      return error(
        exception.response.status == 404 ? 404 : 500,
        exception.message,
      );
    }
    return error(500, "Unknown error");
  }
};
