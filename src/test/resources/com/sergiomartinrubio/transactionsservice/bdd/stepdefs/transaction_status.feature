Feature: Return transaction status
  As an user
  I want to be able to retrieve the status of a transaction
  So that I can see the transaction status, amount and fee

  Scenario: Get transaction that is not stored
    Given a transaction that is not stored in our system
    When I check the status from any channel
    Then the system returns the status INVALID

  Scenario: Get transaction with date before today given a channel
    Given a transaction that is stored in our system with date before today
    When I check the status from a channel:
      | channel  |
      | CLIENT   |
      | ATM      |
      | INTERNAL |
    Then the status, amount and fees are:
      | status  | amount | fee  |
      | SETTLED | 190.20 |      |
      | SETTLED | 190.20 |      |
      | SETTLED | 193.38 | 3.18 |


  Scenario: Get transaction with date today given a channel
    Given a transaction that is stored in our system with date today
    When I check the status from a channel:
      | channel  |
      | CLIENT   |
      | ATM      |
      | INTERNAL |
    Then the status, amount and fees are:
      | status  | amount | fee  |
      | PENDING | 190.20 |      |
      | PENDING | 190.20 |      |
      | PENDING | 193.38 | 3.18 |


  Scenario: Get transaction with date after today given a channel
    Given a transaction that is stored in our system with date after today
    When I check the status from a channel:
      | channel  |
      | CLIENT   |
      | ATM      |
      | INTERNAL |
    Then the status, amount and fees are:
      | status  | amount | fee  |
      | FUTURE  | 190.20 |      |
      | PENDING | 190.20 |      |
      | FUTURE  | 193.38 | 3.18 |
