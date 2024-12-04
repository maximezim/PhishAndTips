<script lang="ts">
  import * as Form from "$lib/components/ui/form";
  import { Input } from "$lib/components/ui/input";
  import { formSchema, type FormSchema } from "./schema";
  import {
    type SuperValidated,
    type Infer,
    superForm,
  } from "sveltekit-superforms";
  import { zodClient } from "sveltekit-superforms/adapters";
  
  export let data: SuperValidated<Infer<FormSchema>>;
  
  const form = superForm(data, {
    validators: zodClient(formSchema),
  });
  
  const { form: formData, enhance } = form;

  let show_password = false;
  $: type = show_password ? 'text' : 'password'  
  $: onInput = (e: InputEvent) => {
    if (show_password) {
      $formData.password = (e.target as HTMLInputElement).value;
    }
  }
</script>

<!-- Form component using validation schema from schema.ts -->
<form method="POST" use:enhance class={"flex flex-col gap-2"}>
  <Form.Field {form} name="email">
    <Form.Control let:attrs>
      <Form.Label class={"text-lg"}>Adresse mail</Form.Label>
      <Input {...attrs} bind:value={$formData.email} />
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors />
  </Form.Field>
  <Form.Field {form} name="password">
    <Form.Control let:attrs>
      <Form.Label class={"text-lg"}>Mot de passe</Form.Label>
      <Input { type } {...attrs} on:input={ onInput } bind:value={$formData.password} />
      <input type="checkbox" on:change="{ () => show_password = !show_password }" />  Afficher le mot de passe
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors />
  </Form.Field>
  <Form.Button>Submit</Form.Button>
</form>