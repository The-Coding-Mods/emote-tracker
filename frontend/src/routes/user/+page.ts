import type { PageLoad } from "./$types";
import { UserApi } from "$lib/api/api";

export const load: PageLoad = async ({ fetch }) => {
  const { data: users } = await UserApi.getUsers(fetch);
  return { users };
};
