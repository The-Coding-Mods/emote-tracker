import { Configuration, UserApi } from "$lib/api";
import { BACKEND_URL } from "$lib/common/ApiHost";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ fetch }) => {
  const userApi = new UserApi(
    new Configuration({ basePath: BACKEND_URL, fetchApi: fetch }),
  );
  const users = await userApi.getAllUsers();
  return { users };
};
