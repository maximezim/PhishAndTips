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
  $: onInputCurrentPassword = (e: InputEvent) => {
    if (show_password) {
      $formData.current_password = (e.target as HTMLInputElement).value;
    }
  }
  $: onInputNewPassword = (e: InputEvent) => {
    if (show_password) {
      $formData.new_password = (e.target as HTMLInputElement).value;
    }
  }
  $: onInputConfirmPassword = (e: InputEvent) => {
    if (show_password) {
      $formData.confirm_password = (e.target as HTMLInputElement).value;
    }
  }
</script>

<!-- Form component using validation schema from schema.ts -->
<form method="POST" use:enhance class={"flex flex-col gap-2"}>
  <Form.Field {form} name="current_password">
    <Form.Control let:attrs>
      <Form.Label class={"text-base"}>Mot de passe actuel</Form.Label>
      <Input { type } {...attrs} on:input={ onInputCurrentPassword } bind:value={$formData.current_password} />
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors class="w-96" />
  </Form.Field>
  <Form.Field {form} name="new_password">
    <Form.Control let:attrs>
      <Form.Label class={"text-base"}>Nouveau mot de passe</Form.Label>
      <Input { type } {...attrs} on:input={ onInputNewPassword } bind:value={$formData.new_password} />
    </Form.Control>
    <Form.Description />
    <Form.FieldErrors class="w-96" />
  </Form.Field>
  <Form.Field {form} name="confirm_password">
    <Form.Control let:attrs>
      <Form.Label class={"text-base"}>Confirmer le mot de passe</Form.Label>
      <Input { type } {...attrs} on:input={ onInputConfirmPassword } bind:value={$formData.confirm_password} />
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
    <Form.FieldErrors class="w-full" />
  </Form.Field>
  <Form.Button class="mt-3">Valider</Form.Button>
</form>