import { Configuration, ResponseError, UserApi } from "$lib/api";
import type { LayoutServerLoad } from "./$types";
import { error } from "@sveltejs/kit";
import { BACKEND_BASE_PATH } from "$lib/common/ApiHost";

export const load: LayoutServerLoad = async ({ params, fetch }) => {
	const userApi = new UserApi(new Configuration({ basePath: BACKEND_BASE_PATH, fetchApi: fetch }));

	try {
		const user = await userApi.getUser({ userId: params.id });
		return { user: user };
	} catch (exception) {
		if (exception instanceof ResponseError) {
			return error(exception.response.status == 404 ? 404 : 500, exception.message);
		}
		return error(500, "Unknown error");
	}
};
