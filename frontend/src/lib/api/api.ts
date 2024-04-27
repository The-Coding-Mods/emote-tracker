import createClient from "openapi-fetch";
import type { paths } from "$lib/api/generated";
import { BACKEND_URL } from "$lib/common/ApiHost";

const client = createClient<paths>({ baseUrl: BACKEND_URL });
type fetchType = {
  (input: URL | RequestInfo, init?: RequestInit | undefined): Promise<Response>;
  (
    input: string | Request | URL,
    init?: RequestInit | undefined,
  ): Promise<Response>;
  (input: URL | RequestInfo, init?: RequestInit | undefined): Promise<Response>;
  (
    input: string | Request | URL,
    init?: RequestInit | undefined,
  ): Promise<Response>;
};

export class UserApi {
  static updateEmotes(id: string, customFetch: fetchType) {
    return client.PATCH("/user/{userId}/emotes/update", {
      params: { path: { userId: id } },
      fetch: customFetch,
    });
  }

  static getTopEmotes(id: string, count: number, customFetch: fetchType) {
    return client.GET("/user/{userId}/emotes/count/top", {
      params: { path: { userId: id }, query: { count } },
      fetch: customFetch,
    });
  }

  static getBottomEmotes(id: string, count: number, customFetch: fetchType) {
    return client.GET("/user/{userId}/emotes/count/bottom", {
      params: { path: { userId: id }, query: { count } },
      fetch: customFetch,
    });
  }

  static async getUser(id: string, customFetch?: fetchType) {
    return client.GET("/user/{userId}", {
      params: { path: { userId: id } },
      fetch: customFetch,
    });
  }

  static getUsers(customFetch: fetchType) {
    return client.GET("/user", { fetch: customFetch });
  }
}
