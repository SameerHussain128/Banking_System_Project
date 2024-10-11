import java.util.*;
import java.io.*;

public class AccountManager {
    private HashMap<String, BankAccount> accounts;
    private final String filePath = "data/accounts.txt";

    public AccountManager() {
        accounts = new HashMap<>();
        loadAccountsFromFile();
    }

    public void createAccount(Scanner scanner) {
        System.out.print("Enter Account Holder Name: ");
        String name = scanner.next();

        System.out.print("Set a Password: ");
        String password = scanner.next();

        String accountNumber = UUID.randomUUID().toString();
        BankAccount account = new BankAccount(accountNumber, name, password);
        accounts.put(accountNumber, account);
        saveAccountToFile(account);
        System.out.println("Account created successfully! Your account number is: " + accountNumber);
    }

    public void login(Scanner scanner) {
        System.out.print("Enter Account Number: ");
        String accountNumber = scanner.next();
        BankAccount account = accounts.get(accountNumber);

        if (account == null) {
            System.out.println("Account not found.");
            return;
        }

        System.out.print("Enter Password: ");
        String password = scanner.next();

        if (account.verifyPassword(password)) {
            manageAccount(scanner, account);
        } else {
            System.out.println("Invalid password.");
        }
    }

    private void manageAccount(Scanner scanner, BankAccount account) {
        boolean loggedIn = true;
        while (loggedIn) {
            System.out.println("\nWelcome " + account.getAccountHolderName());
            System.out.println("1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Transfer Funds");
            System.out.println("5. Mini Statement");
            System.out.println("6. Logout");
            System.out.print("Choose an option: ");
            int option = scanner.nextInt();

            switch (option) {
                case 1:
                    System.out.println("Your balance is: $" + account.getBalance());
                    break;
                case 2:
                    System.out.print("Enter amount to deposit: ");
                    double depositAmount = scanner.nextDouble();
                    account.deposit(depositAmount);
                    System.out.println("Deposit successful.");
                    break;
                case 3:
                    System.out.print("Enter amount to withdraw: ");
                    double withdrawAmount = scanner.nextDouble();
                    if (account.withdraw(withdrawAmount)) {
                        System.out.println("Withdrawal successful.");
                    } else {
                        System.out.println("Insufficient balance.");
                    }
                    break;
                case 4:
                    System.out.print("Enter target account number: ");
                    String targetAccountNumber = scanner.next();
                    BankAccount targetAccount = accounts.get(targetAccountNumber);

                    if (targetAccount != null) {
                        System.out.print("Enter amount to transfer: ");
                        double transferAmount = scanner.nextDouble();
                        if (account.withdraw(transferAmount)) {
                            targetAccount.deposit(transferAmount);
                            account.addTransaction(new Transaction("Transfer to " + targetAccountNumber, transferAmount));
                            targetAccount.addTransaction(new Transaction("Transfer from " + account.getAccountNumber(), transferAmount));
                            System.out.println("Transfer successful.");
                        } else {
                            System.out.println("Insufficient balance.");
                        }
                    } else {
                        System.out.println("Target account not found.");
                    }
                    break;
                case 5:
                    MiniStatement.print(account);
                    break;
                case 6:
                    loggedIn = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void loadAccountsFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] details = line.split(",");
                String accountNumber = details[0];
                String name = details[1];
                String password = details[2];
                double balance = Double.parseDouble(details[3]);
                BankAccount account = new BankAccount(accountNumber, name, password);
                accounts.put(accountNumber, account);
            }
        } catch (IOException e) {
            System.out.println("Error loading accounts.");
        }
    }

    private void saveAccountToFile(BankAccount account) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(account.getAccountNumber() + "," + account.getAccountHolderName() + "," + account.getPassword() + "," + account.getBalance());
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving account.");
        }
    }
}
