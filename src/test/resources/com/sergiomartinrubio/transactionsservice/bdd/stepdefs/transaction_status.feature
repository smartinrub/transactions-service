Feature: Return transaction status
  As an user
  I want to be able to retrieve the status of a transaction
  So that I can see the transaction status, amount and fee

  Scenario: Get transaction and transaction is not stored
    Given A transaction that is not stored in our system
    When I check the status from any channel
    Then The system returns the status INVALID

  Scenario: Get transaction with channel CLIENT and transaction is stored and before today
    Given A transaction that is stored in our system with date before today
    When I check the status from CLIENT channel
    Then The system returns the status SETTLED
    And And the amount substracting the fee

  Scenario: Get transaction with channel AMT and transaction is stored and before today
    Given A transaction that is stored in our system with date before today
    When I check the status from ATM channel
    Then The system returns the status SETTLED
    And And the amount substracting the fee

  Scenario: Get transaction with channel INTERNAL and transaction is stored and before today
    Given A transaction that is stored in our system with date before today
    When I check the status from INTERNAL channel
    Then The system returns the status SETTLED
    And And the amount
    And And the fee

  Scenario: Get transaction with channel CLIENT and transaction is stored and today
    Given A transaction that is stored in our system with date today
    When I check the status from CLIENT channel
    Then The system returns the status PENDING
    And And the amount substracting the fee

  Scenario: Get transaction with channel AMT and transaction is stored and today
    Given A transaction that is stored in our system with date today
    When I check the status from ATM channel
    Then The system returns the status PENDING
    And And the amount substracting the fee

  Scenario: Get transaction with channel INTERNAL and transaction is stored and today
    Given A transaction that is stored in our system with date today
    When I check the status from INTERNAL channel
    Then The system returns the status PENDING
    And And the amount
    And And the fee

  Scenario: Get transaction with channel CLIENT and transaction is stored and greater than today
    Given A transaction that is stored in our system with date after today
    When I check the status from CLIENT channel
    Then The system returns the status FUTURE
    And And the amount substracting the fee

  Scenario: Get transaction with channel ATM and transaction is stored and greater than today
    Given A transaction that is stored in our system with date after today
    When I check the status from ATM channel
    Then The system returns the status PENDING
    And And the amount substracting the fee

  Scenario: Get transaction status with channel INTERNAL and transaction is stored and greater than today
    Given A transaction that is stored in our system with date after today
    When I check the status from INTERNAL channel
    Then The system returns the status FUTURE
    And And the amount
    And And the fee
