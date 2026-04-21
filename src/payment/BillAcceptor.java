package payment;

public class BillAcceptor implements MoneyAcceptor {

    @Override
    public String getActionKey() {
        return "k";
    }

    @Override
    public String getActionTitle() {
        return "Пополнить баланс купюрой";
    }

    @Override
    public String getPrompt() {
        return "Введите номинал купюры (50 или 100):";
    }

    @Override
    public int accept(String input) {
        switch (input) {
            case "50":
                return 50;
            case "100":
                return 100;
            default:
                throw new IllegalArgumentException("Купюроприемник принимает только купюры 50 и 100.");
        }
    }
}
