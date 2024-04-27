import type { PageLoad } from "./$types";
import { error } from "@sveltejs/kit";
import { UserApi } from "$lib/api/api";

export const load: PageLoad = async ({ params, fetch }) => {
  const { data: topEmotes, error: topEmotesError } = await UserApi.getTopEmotes(
    params.id,
    50,
    fetch,
  );
  const { data: bottomEmotes, error: bottomEmotesError } =
    await UserApi.getBottomEmotes(params.id, 50, fetch);
  if (topEmotesError || bottomEmotesError) {
    return error(500, "Unknown error");
  }
  return { topEmotes, bottomEmotes };
};
