import type { PageLoad } from "./$types";
import { error } from "@sveltejs/kit";
import client from "$lib/api/api";

export const load: PageLoad = async ({ params, fetch }) => {
  const { data: topEmotes, error: topEmotesError } = await client.GET(
    "/user/{userId}/emotes/count/top",
    {
      params: { path: { userId: params.id }, query: { count: 50 } },
      fetch,
    },
  );
  const { data: bottomEmotes, error: bottomEmotesError } = await client.GET(
    "/user/{userId}/emotes/count/bottom",
    {
      params: { path: { userId: params.id }, query: { count: 50 } },
      fetch,
    },
  );
  if (topEmotesError || bottomEmotesError) {
    return error(500, "Unknown error");
  }
  return { topEmotes, bottomEmotes };
};
