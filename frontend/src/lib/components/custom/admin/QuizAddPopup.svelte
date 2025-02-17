<script lang="ts">
  import * as AlertDialog from "$lib/components/ui/alert-dialog";
  import Button from "$lib/components/ui/button/button.svelte";
  import { showSuccessToast, showErrorToast } from "$lib/toast";
  import ConfirmPopup from "$lib/components/custom/ConfirmPopup.svelte";

  let title: string = "";
  let description: string = "";
  let quizJson: string = "";
  let errorMsg: string = "";

  async function createQuiz() {
    // Basic validation: ensure quiz json is not empty
    if (!quizJson.trim()) {
      errorMsg = "Le contenu du quiz est obligatoire.";
      return;
    }
    errorMsg = "";
    try {
      // Parse the quiz JSON string into an object
      const quizObject = JSON.parse(quizJson);
      const response = await fetch('/api/formation/quiz/createQuiz', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ json: quizObject, title, description }),
      });
      const data = await response.json();
      if (response.ok) {
        showSuccessToast("Quiz créé avec succès");
        window.location.reload();
      } else {
        showErrorToast(data.error || "Erreur lors de la création du quiz");
      }
    } catch (e) {
      console.error(e);
      showErrorToast("Erreur interne lors de la création du quiz");
    }
  }

  function closeAlertDialog() {
    // ...existing code...
    window.location.reload();
  }
</script>

<AlertDialog.Root>
  <AlertDialog.Trigger asChild let:builder>
    <Button class="bg-accent py-0 px-3 text-accent-foreground flex flex-row align-middle gap-1" builders={[builder]}>
      Ajouter Quiz
    </Button>
  </AlertDialog.Trigger>
  <AlertDialog.Portal>
    <AlertDialog.Content class="max-w-full lg:max-w-[60vw] max-h-[90vh] flex flex-col overflow-auto">
      <AlertDialog.Header>
        <AlertDialog.Title>Ajouter un quiz</AlertDialog.Title>
      </AlertDialog.Header>
      <div class="grid grid-cols-1 p-1 w-full gap-y-4 pt-5">
        <div class="flex flex-col gap-2">
          <p class="text-sm">Titre du quiz</p>
          <input bind:value={title} type="text" placeholder="Entrez le titre du quiz" class="border p-2 w-full bg-white"/>
        </div>
        <div class="flex flex-col gap-2">
          <p class="text-sm">Description du quiz</p>
          <textarea bind:value={description} placeholder="Entrez la description du quiz" class="border p-2 w-full bg-white h-20"></textarea>
        </div>
        <div class="flex flex-col gap-2">
          <p class="text-sm">Contenu du quiz (au format JSON)</p>
          <textarea bind:value={quizJson} placeholder="Entrez le contenu du quiz" class="border p-2 w-full h-40 bg-white"></textarea>
          {#if errorMsg}
            <p class="text-red-500 text-sm">{errorMsg}</p>
          {/if}
        </div>
      </div>
      <AlertDialog.Footer class="flex justify-end gap-4">
        <AlertDialog.Cancel>Annuler</AlertDialog.Cancel>
        <ConfirmPopup description="Création du quiz" name="Créer" style="bg-accent" functionToCall={createQuiz} />
      </AlertDialog.Footer>
    </AlertDialog.Content>
  </AlertDialog.Portal>
</AlertDialog.Root>
