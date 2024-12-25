<script lang="ts">
    import CalendarIcon from "lucide-svelte/icons/calendar";
    import {
    DateFormatter,
    parseDate,
    today,
    type DateValue,
    getLocalTimeZone,
  } from "@internationalized/date";
    import { cn } from "$lib/utils.js";
    import { Button } from "$lib/components/ui/button/index.js";
    import { Calendar } from "$lib/components/ui/calendar/index.js";
    import * as Popover from "$lib/components/ui/popover/index.js";

    export let disabled: boolean = false;
    export let value: DateValue | undefined = undefined;
    
    const df = new DateFormatter("fr-FR", {
     dateStyle: "long"
    });
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    const minDate: DateValue = parseDate(tomorrow.toISOString().split('T')[0]);
  
    let width: string | undefined = "w-full";

    $: if (disabled) {
      value = undefined;
    }

   </script>
    
   <Popover.Root>
    <Popover.Trigger asChild let:builder>
     <Button
      variant="outline"
      class={cn(
        `justify-start text-left font-normal ${width}`,
       !value && "text-muted-foreground"
      )}
      disabled={disabled}
      builders={[builder]}
     >
      <CalendarIcon class="mr-2 h-4 w-4" />
      {value ? df.format(value.toDate(getLocalTimeZone())) : "Choisir une date"}
     </Button>
    </Popover.Trigger>
    <Popover.Content class="w-auto p-0">
     <Calendar bind:value minValue={minDate} initialFocus />
    </Popover.Content>
   </Popover.Root>