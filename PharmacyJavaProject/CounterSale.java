// 7 % discount if the customer is 60 or over (applied to the total, packaging included).
public class CounterSale extends Sale {

    // Fixed packaging fee (static final = shared constant)
    public static final double PACKAGING_FEE = 1000;

    public CounterSale(String id, String customerName, int customerAge,
                       int quantity, double unitPrice) throws InvalidSaleException {
        super(id, customerName, customerAge, quantity, unitPrice);
    }

    // Amount before the discount: quantity x price + packaging
    @Override
    public double chargeBeforeDiscount() {
        return (getQuantity() * getUnitPrice()) + PACKAGING_FEE;
    }

    @Override
    public double calculateCharge() {
        double total = chargeBeforeDiscount();
        if (isDiscountApplies()) {
            total = total - (total * DISCOUNT_RATE);
        }
        return total;
    }

    @Override
    public String getSaleType() {
        return "Counter";
    }
}
