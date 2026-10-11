// ABSTRACT class Sale (one sale in the pharmacy).
// - "abstract": we cannot write "new Sale(...)" directly.
//   We create a PrescriptionSale or a CounterSale instead.
// - "implements Chargeable": it follows the contract of the interface.
// It holds everything that is COMMON to both types of sale.
public abstract class Sale implements Chargeable {

    // ----- Constants (values that never change) -----
    // My discount rate: P = B + 5 = 2 + 5 = 7 %  (0.07 as a decimal number)
    public static final double DISCOUNT_RATE = 0.07;
    // The discount applies to customers aged 60 or over
    public static final int DISCOUNT_AGE = 60;

    // Accepted limits (also used by the InputHelper class)
    public static final int MAX_NAME_LENGTH = 50;
    public static final int MIN_AGE = 0;
    public static final int MAX_AGE = 120;
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10000;
    public static final double MIN_PRICE = 1;
    public static final double MAX_PRICE = 10000000;

    // ----- PRIVATE fields (encapsulation) -----
    // "private": nobody outside this class can touch them directly.
    private String id;               // example: TMA625-001
    private String customerName;     // customer name
    private int customerAge;         // customer age
    private int quantity;            // number of items sold
    private double unitPrice;        // price of one item
    private boolean discountApplies; // true if the customer is 60 or over

    // ----- Constructor -----
    // "throws InvalidSaleException": the constructor can throw this exception
    // if a value is invalid.
    public Sale(String id, String customerName, int customerAge,
                int quantity, double unitPrice) throws InvalidSaleException {
        // We use the setters so that the checking is done in one single place.
        setId(id);
        setCustomerName(customerName);
        setCustomerAge(customerAge);   // this setter also works out discountApplies
        setQuantity(quantity);
        setUnitPrice(unitPrice);
    }

    // ----- Getters (read the fields) -----
    public String getId() {
        return id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getCustomerAge() {
        return customerAge;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public boolean isDiscountApplies() {
        return discountApplies;
    }

    // ----- Setters (change the fields) with validation -----
    public void setId(String id) throws InvalidSaleException {
        // id == null: no text at all. trim() removes spaces at the start and the end.
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidSaleException("The ID cannot be blank.");
        }
        this.id = id.trim();
    }

    public void setCustomerName(String customerName) throws InvalidSaleException {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidSaleException("The customer name cannot be blank.");
        }
        if (customerName.trim().length() > MAX_NAME_LENGTH) {
            throw new InvalidSaleException("The customer name cannot be longer than "
                    + MAX_NAME_LENGTH + " characters.");
        }
        this.customerName = customerName.trim();
    }

    public void setCustomerAge(int customerAge) throws InvalidSaleException {
        if (customerAge < MIN_AGE || customerAge > MAX_AGE) {
            throw new InvalidSaleException("The age must be between "
                    + MIN_AGE + " and " + MAX_AGE + ".");
        }
        this.customerAge = customerAge;
        // Here we decide if the discount applies: age 60 or over.
        this.discountApplies = (customerAge >= DISCOUNT_AGE);
    }

    public void setQuantity(int quantity) throws InvalidSaleException {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new InvalidSaleException("The quantity must be between "
                    + MIN_QUANTITY + " and " + MAX_QUANTITY + ".");
        }
        this.quantity = quantity;
    }

    public void setUnitPrice(double unitPrice) throws InvalidSaleException {
        // Double.isNaN: checks that it is not "Not a Number" (a meaningless value).
        // Double.isInfinite: checks that it is not infinity.
        if (Double.isNaN(unitPrice) || Double.isInfinite(unitPrice)
                || unitPrice < MIN_PRICE || unitPrice > MAX_PRICE) {
            throw new InvalidSaleException("The unit price must be between "
                    + (long) MIN_PRICE + " and " + (long) MAX_PRICE + " UGX.");
        }
        this.unitPrice = unitPrice;
    }

    // ----- ABSTRACT methods: no code here -----
    // Each subclass MUST write them with its own rule.

    // (calculateCharge() comes from the Chargeable interface. We do not rewrite it here:
    //  it stays abstract and the subclasses write the code.)

    // Amount of the sale BEFORE the discount
    public abstract double chargeBeforeDiscount();

    // Name of the type of sale, used for display ("Prescription" or "Counter")
    public abstract String getSaleType();

    // ----- Method shared by all sales -----
    // Discount given = amount before discount - final amount.
    public double getDiscountAmount() {
        return chargeBeforeDiscount() - calculateCharge();
    }

    // Text displayed for a sale (used in the report and in the search).
    // This method calls calculateCharge(): this is POLYMORPHISM,
    // because Java picks the right version according to the real type of the sale.
    @Override
    public String toString() {
        String discountText = "no";
        if (discountApplies) {
            discountText = "yes (" + (int) (DISCOUNT_RATE * 100) + "%)";
        }
        return String.format("%s | %-12s | %-20s | age %3d | qty %5d | price %,12.2f | discount: %-8s | CHARGE: %,14.2f UGX",
                id, getSaleType(), customerName, customerAge, quantity,
                unitPrice, discountText, calculateCharge());
    }
}
