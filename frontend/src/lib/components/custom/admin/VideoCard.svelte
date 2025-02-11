<script lang="ts">
  import * as Card from '$lib/components/ui/card';
  import 'iconify-icon';
	import UserAddPopup from './UserAddPopup.svelte';
</script>

<Card.Root class="col-span-10 row-span-2">
  <Card.Header class="flex flex-col gap-3 space-y-0">
    <Card.Title class="text-lg font-semibold flex items-center gap-3 justify-between">
      <span>Vidéos</span>
      <div class="flex flex-row gap-2 align-middle">
        <UserAddPopup />
        <iconify-icon class="text-3xl text-accent flex flex-col align-middle" icon="mingcute:group-3-fill"></iconify-icon>
      </div>
      
    </Card.Title>
  </Card.Header>
  <Card.Content class="flex flex-col gap-6">
  </Card.Content>
</Card.Root>
