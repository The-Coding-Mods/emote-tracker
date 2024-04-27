import createClient from "openapi-fetch";
import type { paths } from "$lib/api/generated";
import { BACKEND_URL } from "$lib/common/ApiHost";

const client = createClient<paths>({ baseUrl: BACKEND_URL });
export default client;
