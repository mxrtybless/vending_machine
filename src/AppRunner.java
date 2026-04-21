import enums.ActionLetter;
import model.*;
import payment.BillAcceptor;
import payment.CoinAcceptor;
import payment.MoneyAcceptor;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();
    private final UniversalArray<MoneyAcceptor> moneyAcceptors = new UniversalArrayImpl<>();
    private final Scanner scanner = new Scanner(System.in);

    private int balance;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });

        moneyAcceptors.add(new CoinAcceptor());
        moneyAcceptors.add(new BillAcceptor());
        balance = 0;
    }

    public static void run() {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }

    private void startSimulation() {
        print("В автомате доступны:");
        showProducts(products);

        print("Баланс: " + balance);

        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (balance >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private void chooseAction(UniversalArray<Product> allowedProducts) {
        showMoneyAcceptorActions();
        showActions(allowedProducts);
        print(" h - Выйти");

        String action = fromConsole().substring(0, 1);

        if (handleMoneyInput(action)) {
            return;
        }

        try {
            for (int i = 0; i < allowedProducts.size(); i++) {
                if (allowedProducts.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    balance -= allowedProducts.get(i).getPrice();
                    print("Вы купили " + allowedProducts.get(i).getName());
                    return;
                }
            }
        } catch (IllegalArgumentException e) {
            if ("h".equalsIgnoreCase(action)) {
                isExit = true;
            } else {
                print("Недопустимая буква. Попробуйте еще раз.");
                chooseAction(allowedProducts);
            }
            return;
        }

        if ("h".equalsIgnoreCase(action)) {
            isExit = true;
        } else {
            print("Недопустимая буква. Попробуйте еще раз.");
            chooseAction(allowedProducts);
        }
    }

    private boolean handleMoneyInput(String action) {
        for (int i = 0; i < moneyAcceptors.size(); i++) {
            MoneyAcceptor moneyAcceptor = moneyAcceptors.get(i);
            if (moneyAcceptor.getActionKey().equalsIgnoreCase(action)) {
                print(moneyAcceptor.getPrompt());
                String input = fromConsole();
                try {
                    int acceptedAmount = moneyAcceptor.accept(input);
                    balance += acceptedAmount;
                    print("Баланс пополнен на " + acceptedAmount);
                } catch (IllegalArgumentException e) {
                    print(e.getMessage());
                }
                return true;
            }
        }
        return false;
    }

    private void showMoneyAcceptorActions() {
        for (int i = 0; i < moneyAcceptors.size(); i++) {
            MoneyAcceptor moneyAcceptor = moneyAcceptors.get(i);
            print(" " + moneyAcceptor.getActionKey() + " - " + moneyAcceptor.getActionTitle());
        }
    }

    private void showActions(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(String.format(" %s - %s", products.get(i).getActionLetter().getValue(), products.get(i).getName()));
        }
    }

    private String fromConsole() {
        return scanner.nextLine();
    }

    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            print(products.get(i).toString());
        }
    }

    private void print(String msg) {
        System.out.println(msg);
    }
}
