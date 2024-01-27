export function capitalizeFirstLetter(string: string) {
  return string.charAt(0).toUpperCase() + string.slice(1);
}

export function truncate(input: string, length: number) {
  return input.length > length ? `${input.substring(0, length)}...` : input;
}
