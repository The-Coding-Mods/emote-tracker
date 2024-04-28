import type { RequestHandler } from "@sveltejs/kit";
import { BACKEND_URL } from "$lib/common/ApiHost";

export const GET: RequestHandler = ({ params, url, fetch , request}) => {
  return fetch(`${BACKEND_URL}/${params.path + url.search}`, {headers : request.headers, method: "GET", body: request.body});
};
export const POST: RequestHandler = ({ params, url, fetch , request}) => {
  return fetch(`${BACKEND_URL}/${params.path + url.search}`, {headers : request.headers, method: "POST", body: request.body});
};
export const PATCH: RequestHandler = ({ params, url, fetch , request}) => {
  return fetch(`${BACKEND_URL}/${params.path + url.search}`, {headers : request.headers, method: "PATCH", body: request.body});
};
export const OPTIONS: RequestHandler = ({ params, url, fetch, request }) => {
  return fetch(`${BACKEND_URL}/${params.path + url.search}`, {headers : request.headers, method: "OPTIONS", body: request.body});
};
