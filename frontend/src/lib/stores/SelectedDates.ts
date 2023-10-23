import type { Writable } from "svelte/store";
import { writable } from "svelte/store";
import { browser } from "$app/environment";
import { isoDateString } from "$lib/service/DateUtil";

export type Range<T> = {
  from: T;
  to: T;
};

let storedDates: Range<string> = { from: "", to: "" };

if (browser) {
  storedDates = JSON.parse(localStorage.getItem("selectedDates")!);
}
export const selectedDates: Writable<Range<Date | undefined>> = writable({
  from: storedDates?.from ? new Date(storedDates.from) : undefined,
  to: storedDates?.to ? new Date(storedDates.to) : undefined,
});

selectedDates.subscribe((value) => {
  if (browser) {
    localStorage.setItem(
      "selectedDates",
      JSON.stringify({
        from: isoDateString(value.from),
        to: isoDateString(value.to),
      }),
    );
  }
});
export const prioritizeStart: Writable<boolean> = writable(true);
