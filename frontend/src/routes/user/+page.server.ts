import type { PageServerLoad } from "./$types";
import { UserApi } from "$lib/api/api";

export const load: PageServerLoad = async ({ fetch }) => {
  const { data: users } = await UserApi.getUsers(fetch);
  return { users };
};
