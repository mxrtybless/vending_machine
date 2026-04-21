package payment;

public interface MoneyAcceptor {
    String getActionKey();
    String getActionTitle();
    String getPrompt();
    int accept(String input);
}
