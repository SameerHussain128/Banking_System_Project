public class MiniStatement {
    public static void print(BankAccount account) {
        System.out.println("Mini Statement for " + account.getAccountHolderName());
        for (Transaction transaction : account.getTransactions()) {
            System.out.println(transaction.getType() + ": $" + transaction.getAmount());
        }
    }
}
