import { error } from "@sveltejs/kit";
import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";
import { Configuration, UserApi } from "$lib/api";
import type { PageServerLoad } from "./$types";

export const load: PageServerLoad = async ({ params, fetch }) => {
	const userApi = new UserApi(
		new Configuration({
			basePath: BACKEND_BASE_PATH,
			fetchApi: fetch,
		}),
	);
	try {
		const topEmotes = userApi.getTopEmotes({
			userId: params.id,
			count: 50,
		});
		const bottomEmotes = userApi.getBottomEmotes({
			userId: params.id,
			count: 50,
		});
		return { topEmotes, bottomEmotes };
	} catch (exception) {
		return error(500, "Unknown error");
	}
};
