<script lang="js">
  import "survey-js-ui";
  import "survey-core/defaultV2.min.css";
  import { onMount } from 'svelte';
  import * as SurveyCore from 'survey-core';
  const { Model } = SurveyCore;
  const { settings } = SurveyCore;


  

  /** @type {HTMLDivElement|null} */
  export let surveyElement = null;

  /** @type {JSON} */
  export let json;

  const survey = new Model(json);
  
  onMount(() => {
		survey.render(surveyElement);
	});

</script>

<div bind:this={surveyElement} class="survey-container"></div> 