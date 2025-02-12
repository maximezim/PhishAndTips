<script lang="js">
  import { onMount } from 'svelte';
  import { get } from 'svelte/store';
  import { page } from '$app/stores';
  import AuthService from '$lib/services/AuthService';
  import surveyTheme from './surveyTheme.json';

  /**
	 * @type {HTMLDivElement}
	 */
  let surveyElement;
  let quiz;

  // Récupère l'id depuis le store page
  const id = get(page).params.id;


    // `/api/formation/asset?assetURL=${video.thumbnailUrl}`

  /**
	 * @param {string} currentUrl
	 */
  function replaceUrl(currentUrl) {
    return `/api/formation/asset?assetURL=${currentUrl}`
  }

  /**
	 * @param {JSON} quizJson
	 */
  function replaceAllUrls(quizJson) {
    
  }

  onMount(async () => {
    const { Model } = await import("survey-core");
    await import("survey-js-ui");
    await import("survey-core/defaultV2.min.css");
    
    quiz = await fetch(`/api/formation/quiz/id?quizId=${id}`).then(res => res.json());
    let json = JSON.parse(quiz.json);

    console.log(json);
    
    const survey = new Model(json);

    // @ts-ignore
    survey.applyTheme(surveyTheme);
    survey.render(surveyElement);
  });
</script>

<div bind:this={surveyElement} class="survey-container"></div>
