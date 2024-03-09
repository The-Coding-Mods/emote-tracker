import { Configuration, UserApi } from "$lib/api";
import { BACKEND_URL } from "$lib/common/ApiHost";
import type { PageServerLoad } from "./$types";

const userApi = new UserApi(new Configuration({ basePath: BACKEND_URL }));

export const load: PageServerLoad = async () => {
  const users = await userApi.getAllUsers();
  return { users };
};
