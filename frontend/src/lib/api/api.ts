import createClient from "openapi-fetch";
import type { paths } from "$lib/api/generated";
import { BACKEND_URL } from "$lib/common/ApiHost";

const client = createClient<paths>({ baseUrl: BACKEND_URL });

export class UserApi {
  static updateEmotes(
    id: string,
    customFetch?: (request: Request) => ReturnType<typeof fetch>,
  ) {
    return client.PATCH("/user/{userId}/emotes/update", {
      params: { path: { userId: id } },
      fetch: customFetch,
    });
  }

  static getTopEmotes(
    id: string,
    count: number,
    customFetch?: (request: Request) => ReturnType<typeof fetch>,
  ) {
    return client.GET("/user/{userId}/emotes/count/top", {
      params: { path: { userId: id }, query: { count } },
      fetch: customFetch,
    });
  }

  static getBottomEmotes(
    id: string,
    count: number,
    customFetch?: (request: Request) => ReturnType<typeof fetch>,
  ) {
    return client.GET("/user/{userId}/emotes/count/bottom", {
      params: { path: { userId: id }, query: { count } },
      fetch: customFetch,
    });
  }

  static getUser(
    id: string,
    customFetch?: (request: Request) => ReturnType<typeof fetch>,
  ) {
    return client.GET("/user/{userId}", {
      params: { path: { userId: id } },
      fetch: customFetch,
    });
  }

  static getUsers(
    customFetch?: (request: Request) => ReturnType<typeof fetch>,
  ) {
    return client.GET("/user", { fetch: customFetch });
  }
}
