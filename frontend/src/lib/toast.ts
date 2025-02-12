import { toast } from "svelte-sonner";

function getFormattedDate(): string {
  const options: Intl.DateTimeFormatOptions = {
    weekday: "long",
    day: "2-digit",
    month: "long",
    year: "numeric",
    hour: "2-digit",
    minute: "2-digit",
  };

  return new Date().toLocaleDateString("fr-FR", options);
}

export function showSuccessToast(message: string, description?: string) {
  toast.success(message, { description: description ?? getFormattedDate() });
}

export function showErrorToast(message: string, description?: string) {
  toast.error(message, { description: description ?? getFormattedDate() });
}
