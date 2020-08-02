Feature: Return transaction status
  Return status and additional information for a transaction

  Scenario: call to /transactions/{reference}/status and transaction is not stored
    Given A transaction that is not stored in our system
    When I check the status from any channel
    Then The system returns the status 'INVALID'

  Scenario: call to /transactions/{reference}/status with channel CLIENT or AMT and transaction is stored and before today
    Given A transaction that is stored in our system
    When I check the status from 'CLIENT' or 'ATM' channel
    And And the transaction date is before today
    Then The system returns the status 'SETTLED'
    And And the amount substracting the fee

  Scenario: call to /transactions/{reference}/status with channel INTERNAL and transaction is stored and before today
    Given A transaction that is stored in our system
    When I check the status from 'INTERNAL' channel
    And And the transaction date is before today
    Then The system returns the status 'SETTLED'
    And And the amount
    And And the fee

  Scenario: call to /transactions/{reference}/status with channel CLIENT or AMT and transaction is stored and today
    Given A transaction that is stored in our system
    When I check the status from 'CLIENT' or 'ATM' channel
    And And the transaction date is equals to today
    Then The system returns the status 'PENDING'
    And And the amount substracting the fee

  Scenario: call to /transactions/{reference}/status with channel INTERNAL and transaction is stored and today
    Given A transaction that is stored in our system
    When I check the status from 'INTERNAL' channel
    And And the transaction date is equals to today
    Then The system returns the status 'PENDING'
    And And the amount
    And And the fee

  Scenario: call to /transactions/{reference}/status with channel CLIENT and transaction is stored and greater than today
    Given A transaction that is stored in our system
    When I check the status from 'CLIENT' channel
    And And the transaction date is greater than today
    Then The system returns the status 'FUTURE'
    And And the amount substracting the fee

  Scenario: call to /transactions/{reference}/status with channel ATM and transaction is stored and greater than today
    Given A transaction that is stored in our system
    When I check the status from 'ATM' channel
    And And the transaction date is greater than today
    Then The system returns the status 'PENDING'
    And And the amount substracting the fee

  Scenario: call to /transactions/{reference}/status with channel INTERNAL and transaction is stored and greater than today
    Given A transaction that is stored in our system
    When I check the status from 'INTERNAL' channel
    And And the transaction date is greater than today
    Then The system returns the status 'FUTURE'
    And And the amount
    And And the fee
