// First subclass: sale with a prescription.
// Rule: quantity x price. 7 % discount if the customer is 60 or over.
// "extends Sale": inheritance. This class receives all the fields and methods of Sale.
public class PrescriptionSale extends Sale {

    public PrescriptionSale(String id, String customerName, int customerAge,
                            int quantity, double unitPrice) throws InvalidSaleException {
        // super(...): calls the constructor of the parent class (Sale)
        // so that it fills the common fields.
        super(id, customerName, customerAge, quantity, unitPrice);
    }

    // Amount before the discount: quantity x price
    @Override
    public double chargeBeforeDiscount() {
        return getQuantity() * getUnitPrice();
    }

    // @Override: we replace the abstract method with our own rule.
    @Override
    public double calculateCharge() {
        double total = chargeBeforeDiscount();
        // If the customer is 60 or over, we remove 7 % of the total.
        if (isDiscountApplies()) {
            total = total - (total * DISCOUNT_RATE);
        }
        return total;
    }

    @Override
    public String getSaleType() {
        return "Prescription";
    }
}
