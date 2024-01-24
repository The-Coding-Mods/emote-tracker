import { type Emote, type SimpleUser } from "$lib/../api";

type Success = {
    error: false;
    user: SimpleUser;
    topEmotes: Emote[];
    bottomEmotes: Emote[];
    noUsage: Emote[];
};

type Error = {
    error: true;
    statusCode: number;
    message: string;
};
export type UserLoad = Success | Error;
