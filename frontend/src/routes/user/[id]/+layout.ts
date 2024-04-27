import type { LayoutLoad } from "./$types";
import client from "$lib/api/api";
import { error } from "@sveltejs/kit";

export const load: LayoutLoad = async ({ params, fetch }) => {
  const { data: user, response } = await client.GET("/user/{userId}", {
    params: { path: { userId: params.id } },
    fetch,
  });
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
