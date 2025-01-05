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
  import { Checkbox } from "$lib/components/ui/checkbox";
  import { Label } from "$lib/components/ui/label";
  
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

  // handle forget password
  const forgetPassword = () => {
    console.log('forget password');
  }

</script>

<!-- Form component using validation schema from schema.ts -->
<form method="POST" use:enhance class={"flex flex-col gap-2"}>
  <Form.Field {form} name="email">
    <Form.Control let:attrs>
      <Form.Label class={"text-base"}>Adresse mail</Form.Label>
      <Input {...attrs} bind:value={$formData.email} />
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors />
  </Form.Field>
  <Form.Field {form} name="password">
    <Form.Control let:attrs>
      <Form.Label class={"text-base"}>Mot de passe</Form.Label>
      <Input { type } {...attrs} on:input={ onInput } bind:value={$formData.password} />
      <div class="flex items-center gap-2 my-3">
        <Checkbox id="password-toggle" bind:checked={show_password} aria-labelledby="password-toggle-label" />
        <Label
          class="text-sm text-gray-500"
          id="password-label"
          for="password-toggle"
        >
          Afficher le mot de passe.
        </Label>
      </div>
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors />
  </Form.Field>
  <Form.Button class="mt-3">Se connecter</Form.Button>
  <button type="button" on:click={forgetPassword} class={"text-center"}> Mot de passe oublié</button>
</form>

