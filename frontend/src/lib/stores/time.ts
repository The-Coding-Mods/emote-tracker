import { readable } from "svelte/store";
import { DateTime } from "luxon";

export const time = readable(new Date(), function start(set) {
	const interval = setInterval(() => {
		set(new Date());
	}, 1000);

	return function stop() {
		clearInterval(interval);
	};
});

export const dateTime = readable(DateTime.now(), function start(set) {
	const interval = setInterval(() => {
		set(DateTime.now());
	}, 1000);

	return function stop() {
		clearInterval(interval);
	};
});
