import java.util.ArrayList;
import java.util.Locale;

// Main class: contains main() and the menu.
// Scenario of TSHONGO MUHAYIRI Agape (VU-BIT-2511-1625-DAY):
//   A = 6 -> report sorted by charge, from the highest to the lowest
//   B = 2 -> discount P = 2 + 5 = 7 %
//   C = 5 -> Pharmacy: Sale, PrescriptionSale, CounterSale
public class PharmacyApp {

    public static void main(String[] args) {
        // Forces the dot as decimal separator (2500.50 and not 2500,50)
        Locale.setDefault(Locale.US);

        InputHelper input = new InputHelper();
        // The IDs start with my initials (T M A) + 625 (last 3 digits)
        SaleManager manager = new SaleManager("TMA625");

        // Personal touch: full name + registration number at start-up
        System.out.println("==============================================");
        System.out.println("   PHARMACY CHARGING SYSTEM");
        System.out.println("   Name   : TSHONGO MUHAYIRI Agape");
        System.out.println("   Reg No : VU-BIT-2511-1625-DAY");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            showMenu();
            int choice = input.readInt("Choose an option (1-7): ", 1, 7);

            if (choice == 1) {
                addSale(manager, input, true);    // true = PrescriptionSale
            } else if (choice == 2) {
                addSale(manager, input, false);   // false = CounterSale
            } else if (choice == 3) {
                listReport(manager);
            } else if (choice == 4) {
                findSale(manager, input);
            } else if (choice == 5) {
                removeSale(manager, input);
            } else if (choice == 6) {
                showSummary(manager);
            } else if (choice == 7) {
                System.out.println("Goodbye, thank you for using the Pharmacy Charging System!");
                running = false;
            }
        }
    }

    // Shows the menu
    private static void showMenu() {
        System.out.println();
        System.out.println("----------- MENU -----------");
        System.out.println("1. Add a prescription sale");
        System.out.println("2. Add a counter sale");
        System.out.println("3. List the sorted report (highest charge first)");
        System.out.println("4. Find a sale by ID");
        System.out.println("5. Remove a sale");
        System.out.println("6. Show summary (total charges and total discount)");
        System.out.println("7. Exit");
    }

    // Adds a sale. If isPrescription is true -> PrescriptionSale, otherwise CounterSale.
    private static void addSale(SaleManager manager, InputHelper input, boolean isPrescription) {
        if (isPrescription) {
            System.out.println("--- New prescription sale ---");
        } else {
            System.out.println("--- New counter sale ---");
        }

        // We read all the values: the helper asks again as long as the value is invalid
        String name = input.readText("Customer name: ", Sale.MAX_NAME_LENGTH);
        int age = input.readInt("Customer age: ", Sale.MIN_AGE, Sale.MAX_AGE);
        int quantity = input.readInt("Quantity: ", Sale.MIN_QUANTITY, Sale.MAX_QUANTITY);
        double price = input.readDouble("Unit price (UGX): ", Sale.MIN_PRICE, Sale.MAX_PRICE);

        // try / catch: safety net. If the constructor refuses a value,
        // we show the message instead of crashing.
        try {
            String id = manager.nextId();
            Sale sale;   // reference of the abstract type: it can point to either one
            if (isPrescription) {
                sale = new PrescriptionSale(id, name, age, quantity, price);
            } else {
                sale = new CounterSale(id, name, age, quantity, price);
            }
            manager.addSale(sale);

            System.out.println("Sale added successfully with ID " + sale.getId());
            System.out.println("Charge: " + String.format("%,.2f", sale.calculateCharge()) + " UGX");
            if (sale.isDiscountApplies()) {
                System.out.println("A 7% senior discount (age 60+) was applied: -"
                        + String.format("%,.2f", sale.getDiscountAmount()) + " UGX");
            }
        } catch (InvalidSaleException e) {
            System.out.println("Could not add the sale: " + e.getMessage());
        }
    }

    // Shows the sorted report
    private static void listReport(SaleManager manager) {
        if (manager.getCount() == 0) {
            System.out.println("There are no sales to report yet.");
            return;   // we leave the method
        }
        ArrayList<Sale> report = manager.getSortedReport();
        System.out.println("--- SALES REPORT (highest charge first) ---");
        int position = 1;
        for (Sale sale : report) {
            // POLYMORPHISM: we see the sale as a "Chargeable".
            // Java calls the right calculateCharge() (Prescription or Counter).
            Chargeable item = sale;
            double charge = item.calculateCharge();
            System.out.println(position + ". " + sale);
            position++;
        }
    }

    // Looks for a sale by ID
    private static void findSale(SaleManager manager, InputHelper input) {
        String id = input.readText("Enter the sale ID to find: ", 30);
        try {
            Sale sale = manager.findById(id);
            System.out.println("Sale found:");
            System.out.println(sale);
        } catch (SaleNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // Removes a sale after confirmation
    private static void removeSale(SaleManager manager, InputHelper input) {
        String id = input.readText("Enter the sale ID to remove: ", 30);
        try {
            Sale sale = manager.findById(id);   // first check that the ID exists
            System.out.println(sale);
            boolean confirmed = input.readYesNo("Are you sure you want to remove this sale? (y/n): ");
            if (confirmed) {
                manager.removeSale(id);
                System.out.println("Sale " + sale.getId() + " has been removed.");
            } else {
                System.out.println("Removal cancelled. The sale was kept.");
            }
        } catch (SaleNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    // Shows the summary
    private static void showSummary(SaleManager manager) {
        System.out.println("--- SUMMARY ---");
        System.out.println("Number of sales      : " + manager.getCount());
        System.out.println("Total charges        : " + String.format("%,.2f", manager.getTotalCharges()) + " UGX");
        System.out.println("Total discount given : " + String.format("%,.2f", manager.getTotalDiscount()) + " UGX");
    }
}
