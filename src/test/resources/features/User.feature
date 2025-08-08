Feature: User API testing with scenario outline

  Scenario Outline: Create a user
    Given I have user details with id "<id>", username "<username>", firstName "<firstName>", lastName "<lastName>", email "<email>", password "<password>", and phone "<phone>"
    When I create the user
    Then the response status code should be 200

    Examples:
      | id | username    | firstName | lastName | email            | password | phone      |
      | 1  | john_doe_88 | John      | Doe      | john@example.com | pass123  | 1234567890 |

  Scenario Outline: Get the created user
    Given I have a username "<username>"
    When I retrieve the user details
    Then the user response should contain firstName "<firstName>" and lastName "<lastName>"
    And the response status code should be 200

    Examples:
      | username    | firstName | lastName |
      | john_doe_88 | John      | Doe      |

  Scenario Outline: Update the created user
    Given I have updated user details with username "<username>" and firstName "<newFirstName>" and lastName "<newLastName>"
    When I update the user
    Then the response status code should be 200

    Examples:
      | username    | newFirstName | newLastName |
      | john_doe_88 | Johnny       | Don         |

  Scenario Outline: Delete the user
    Given I have a username "<username>" to delete
    When I delete the user
    Then the response status code should be 200

    Examples:
      | username    |
      | john_doe_88 |
  
