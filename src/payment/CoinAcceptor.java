package payment;

public class CoinAcceptor implements MoneyAcceptor {

    @Override
    public String getActionKey() {
        return "a";
    }

    @Override
    public String getActionTitle() {
        return "Пополнить баланс монетами";
    }

    @Override
    public String getPrompt() {
        return "Введите номинал монеты (5 или 10):";
    }

    @Override
    public int accept(String input) {
        switch (input) {
            case "5":
                return 5;
            case "10":
                return 10;
            default:
                throw new IllegalArgumentException("Монетоприемник принимает только монеты 5 и 10.");
        }
    }
}
