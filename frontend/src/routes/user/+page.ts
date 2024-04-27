import type { PageLoad } from "./$types";
import client, { UserApi } from "$lib/api/api";

export const load: PageLoad = async ({ fetch }) => {
  const { data: users } = await client.GET("/user", { fetch });
  return { users };
};
