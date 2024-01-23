import type { LayoutServerLoad } from "./$types";
import { redirect } from "@sveltejs/kit";

export const load: LayoutServerLoad = async ({ params }) => {
  redirect(301, `/user/${params.id}`);
};
