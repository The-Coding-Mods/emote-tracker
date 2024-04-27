import type { LayoutLoad } from "./$types";
import { UserApi } from "$lib/api/api";
import { error } from "@sveltejs/kit";

export const load: LayoutLoad = async ({ params, fetch }) => {
  const { data: user, response } = await UserApi.getUser(params.id, fetch);
  if (response?.status === 404) {
    return error(404, response?.statusText);
  }
  return {
    user: {
      ...user,
      registered: Date.parse(user!.registered),
      lastUpdated: Date.parse(user!.lastUpdated),
    },
  };
};
