export const days: string[] = ["Mo", "Tu", "We", "Th", "Fr", "Sa", "Su"]
export const months: string[] = ["Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"];

export function isoDateString(date: Date | undefined): string {
    if (!date) return ""
    return new Date(date.getTime() - (date.getTimezoneOffset() * 60000)).toISOString().split("T")[0]!;
}
