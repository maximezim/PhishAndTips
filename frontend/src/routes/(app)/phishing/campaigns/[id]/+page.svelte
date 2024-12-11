<script lang="ts">
    import { page } from '$app/stores';
    import { onMount } from 'svelte';
    import { get } from 'svelte/store';
  
    let id: string;
  
    onMount(() => {
      const data = get(page).data;
      if (data && data.id) {
        id = data.id;
      } else {
        console.error('ID is undefined');
      }
    });
  </script>
  
  <h1>Phishing Campaign</h1>
  <p>Phishing campaign id: {id}</p>