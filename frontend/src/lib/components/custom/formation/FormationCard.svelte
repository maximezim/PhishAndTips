<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import 'iconify-icon';
</script>

<Card.Root class="col-span-4 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>A suivre...</span>
      <iconify-icon class="text-3xl text-accent" icon="mingcute:book-6-fill"></iconify-icon>
      </Card.Title>
  </Card.Header>
  <Card.Content>
  </Card.Content>
</Card.Root>