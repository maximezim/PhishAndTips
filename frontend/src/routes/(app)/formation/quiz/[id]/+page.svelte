<script lang="js">
  import { onMount } from 'svelte';
  import { get } from 'svelte/store';
  import { page } from '$app/stores';
  import surveyTheme from './surveyTheme.json';

  // Reference to the survey container element
  /**
   * @type {HTMLDivElement}
   */
  let surveyElement;
  /**
	 * @type {any}
	 */
  let quiz;

  // Get the quiz id from the page store
  const id = get(page).params.id;

  // Function to replace the current URL with the asset URL
  /**
   * @param {any} currentUrl
   */
  function replaceUrl(currentUrl) {
    return `/api/formation/asset?assetURL=${currentUrl}`;
  }

  // Function to traverse the quiz JSON and replace URLs in "logo" and "imageLink" properties
  /**
   * @param {{ logo: string; pages: any[]; }} quizJson
   */
  function replaceAllUrls(quizJson) {
    if (quizJson.logo) {
      quizJson.logo = replaceUrl(quizJson.logo);
    }
    if (Array.isArray(quizJson.pages)) {
      quizJson.pages.forEach(page => {
        if (Array.isArray(page.elements)) {
          page.elements.forEach((/** @type {{ imageLink: string; }} */ element) => {
            if (element.imageLink) {
              element.imageLink = replaceUrl(element.imageLink);
            }
          });
        }
      });
    }
    return quizJson;
  }

  // Function to get the choice text for a given answer from a question
  /**
   * @param {{ choices: any[]; }} question
   * @param {any} answer
   */
  function getChoiceText(question, answer) {
    if (question.choices && question.choices.length > 0) {
      if (typeof question.choices[0] === "object") {
        const found = question.choices.find(choice => choice.value === answer);
        return found && found.text ? found.text : answer;
      }
    }
    return answer;
  }

  let displayAnswers = false;

  onMount(async () => {
    // Import survey-core modules
    const { Model } = await import("survey-core");
    await import("survey-js-ui");
    await import("survey-core/defaultV2.min.css");
    
    // Get quiz from API
    quiz = await fetch(`/api/formation/quiz/id?quizId=${id}`).then(res => res.json());
    let json = JSON.parse(quiz.json);
    json = replaceAllUrls(json);
    
    // Create survey model
    const survey = new Model(json);

    // Add event listener for survey completion
    survey.onComplete.add(async (sender) => {
      const results = sender.data;
      const correctionsDiv = document.getElementById("corrections");
      displayAnswers = true;
      
      // Variables for score calculation
      let totalQuestions = 0;
      let correctCount = 0;

      if (correctionsDiv) {
        // Clear any existing corrections
        correctionsDiv.innerHTML = '';

        Object.keys(results).forEach((key) => {
          // Retrieve the corresponding question from the quiz JSON
          const question = json.pages.flatMap((/** @type {{ elements: any; }} */ p) => p.elements).find((/** @type {{ name: string; }} */ q) => q.name === key);
          if (!question || question.correctAnswer === undefined) return;

          totalQuestions++;

          const userAnswer = results[key];
          const correctAnswer = question.correctAnswer;
          let isCorrect = false;

          // Determine if the user's answer is correct based on question type
          switch (question.type) {
            case "checkbox":
            case "checkgroup":
              if (Array.isArray(userAnswer) && Array.isArray(correctAnswer)) {
                const sortedUser = userAnswer.slice().sort();
                const sortedCorrect = correctAnswer.slice().sort();
                isCorrect = sortedUser.join(',') === sortedCorrect.join(',');
              }
              break;
            case "ranking":
              if (Array.isArray(userAnswer) && Array.isArray(correctAnswer)) {
                isCorrect = userAnswer.join(',') === correctAnswer.join(',');
              }
              break;
            default:
              isCorrect = userAnswer === correctAnswer;
              break;
          }
          
          if (isCorrect) {
            correctCount++;
          }
          
          const color = isCorrect ? "green" : "red";
          let displayUserAnswer = "";
          let displayCorrectAnswer = "";

          // Format the display of answers based on question type
          switch (question.type) {
            case "boolean":
              displayUserAnswer = userAnswer ? "True" : "False";
              displayCorrectAnswer = correctAnswer ? "True" : "False";
              break;
            case "radiogroup":
            case "dropdown":
              displayUserAnswer = getChoiceText(question, userAnswer);
              displayCorrectAnswer = getChoiceText(question, correctAnswer);
              break;
            case "checkbox":
            case "checkgroup":
              if (Array.isArray(userAnswer)) {
                displayUserAnswer = userAnswer.map(val => getChoiceText(question, val)).join(", ");
              } else {
                displayUserAnswer = getChoiceText(question, userAnswer);
              }
              if (Array.isArray(correctAnswer)) {
                displayCorrectAnswer = correctAnswer.map(val => getChoiceText(question, val)).join(", ");
              } else {
                displayCorrectAnswer = getChoiceText(question, correctAnswer);
              }
              break;
            case "ranking":
              if (Array.isArray(userAnswer)) {
                displayUserAnswer = userAnswer.map(val => getChoiceText(question, val)).join(" > ");
              } else {
                displayUserAnswer = getChoiceText(question, userAnswer);
              }
              if (Array.isArray(correctAnswer)) {
                displayCorrectAnswer = correctAnswer.map(val => getChoiceText(question, val)).join(" > ");
              } else {
                displayCorrectAnswer = getChoiceText(question, correctAnswer);
              }
              break;
            case "rating":
            case "text":
            case "comment":
              displayUserAnswer = userAnswer;
              displayCorrectAnswer = correctAnswer;
              break;
            case "matrix":
            case "matrixdropdown":
            case "multipletext":
              displayUserAnswer = JSON.stringify(userAnswer);
              displayCorrectAnswer = JSON.stringify(correctAnswer);
              break;
            case "imagepicker":
              displayUserAnswer = getChoiceText(question, userAnswer);
              displayCorrectAnswer = getChoiceText(question, correctAnswer);
              break;
            default:
              displayUserAnswer = userAnswer;
              displayCorrectAnswer = correctAnswer;
              break;
          }

          // Find the associated explanation from an "expression" element
          const infoElement = json.pages.flatMap((/** @type {{ elements: any; }} */ p) => p.elements)
            .find((/** @type {{ name: string; }} */ q) => q.name === `info${key.replace('question', '')}`);
          const explanation = infoElement ? infoElement.description : "No additional information.";

          // Create the HTML block for the correction
          const correctionHTML = `
            <div class="correction-box">
              <p><strong>${question.title}</strong></p>
              <p>Your answer: <span style="color:${color}">${displayUserAnswer}</span></p>
              <p>Correct answer: <strong>${displayCorrectAnswer}</strong></p>
              <p><em>${explanation}</em></p>
              <hr />
            </div>
          `;

          correctionsDiv.innerHTML += correctionHTML;
        });
      }

      // Log the score to the console: correct answers / total questions
      console.log(`Score: ${correctCount} / ${totalQuestions}`);
      let score = correctCount / totalQuestions;
      await fetch('/api/formation/quiz', {
        method: 'POST',
        body: JSON.stringify({
          score: score,
          quizId: quiz.id
        }),
        headers: {
          'Content-Type': 'application/json'
        }
      });
    });

    // Apply the theme and render the survey
    // @ts-ignore
    survey.applyTheme(surveyTheme);
    survey.render(surveyElement);
  });
</script>

<!-- Survey container -->
<div bind:this={surveyElement} class="survey-container"></div>
<!-- Corrections container -->
<div id="corrections" class="z-10 relative p-6 bg-white"></div>
