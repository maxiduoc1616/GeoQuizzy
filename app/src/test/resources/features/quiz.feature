Feature: Quiz Flow
  Simulate a user playing the quiz

  Scenario: User starts a Normal difficulty quiz
    Given The app is open
    When I tap "Start Quiz"
    And I tap "Normal (3 Vidas)"
    Then I should see the Quiz screen
    And I should see 3 hearts

  Scenario: User answers a question
    Given The app is open
    When I tap "Start Quiz"
    And I tap "Normal (3 Vidas)"
    And I wait for the quiz to start
    When I tap the first answer option
    Then the answer options should become disabled
