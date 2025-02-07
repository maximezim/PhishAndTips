<script lang="ts">
  import * as Carousel from '$lib/components/ui/carousel';
  import * as Card from '$lib/components/ui/card';
  import 'iconify-icon';
</script>

<div class="col-span-8 row-span-1 px-12">
  <Carousel.Root opts={{ align: "start" }} class="w-full h-full">
    <Carousel.Content class="h-full flex items-center">
      {#each Array.from({ length: 3 }, (_, i) => i) as _}
        <Carousel.Item class="flex items-center justify-center md:basis-1/2 lg:basis-1/6">
          <div class="p-1 h-full flex items-center justify-center">
            <div class="h-full aspect-square">
              <Card.Root class="flex h-full w-full items-center justify-center bg-accent">
                <Card.Header>
                </Card.Header>
                <Card.Content>
                </Card.Content>
              </Card.Root>
            </div>
          </div>
        </Carousel.Item>
      {/each}

      {#each Array.from({ length: 7 }, (_, i) => i) as _}
        <Carousel.Item class="flex items-center justify-center md:basis-1/2 lg:basis-1/6">
          <div class="p-1 h-full flex items-center justify-center">
            <div class="h-full aspect-square">
              <Card.Root class="flex h-full w-full items-center justify-center bg-muted">
                <Card.Header>
                </Card.Header>
                <Card.Content>
                </Card.Content>
              </Card.Root>
            </div>
          </div>
        </Carousel.Item>
      {/each}
    </Carousel.Content>
    <Carousel.Previous />
    <Carousel.Next />
  </Carousel.Root>
</div>
