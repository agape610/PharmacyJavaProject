import java.util.ArrayList;
import java.util.Collections;

// "Manager" class: it keeps all the sales in an ArrayList
// and offers the operations: add, find, remove, totals, sorted report.
public class SaleManager {

    // List of all the sales. The type Sale accepts PrescriptionSale AND CounterSale.
    private ArrayList<Sale> sales = new ArrayList<>();

    private String idPrefix;   // example: "TMA625"
    private int counter = 0;   // number of sales already created (used to build the IDs)

    public SaleManager(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    // Builds the next ID: TMA625-001, TMA625-002, etc.
    // %03d = number on 3 digits with zeros in front.
    public String nextId() {
        return idPrefix + "-" + String.format("%03d", counter + 1);
    }

    // Add a sale to the list
    public void addSale(Sale sale) {
        sales.add(sale);
        counter++;   // the next ID will be different
    }

    // Find a sale by its ID.
    // If it does not exist, we throw our custom exception.
    public Sale findById(String id) throws SaleNotFoundException {
        for (Sale sale : sales) {
            // equalsIgnoreCase: compares without caring about upper/lower case
            if (sale.getId().equalsIgnoreCase(id.trim())) {
                return sale;
            }
        }
        throw new SaleNotFoundException("No sale found with ID " + id.trim() + ".");
    }

    // Remove a sale by its ID
    public void removeSale(String id) throws SaleNotFoundException {
        Sale sale = findById(id);   // throws the exception if the ID does not exist
        sales.remove(sale);
    }

    // Total of all the amounts charged (after discount)
    public double getTotalCharges() {
        double total = 0;
        for (Sale sale : sales) {
            total = total + sale.calculateCharge();   // polymorphism
        }
        return total;
    }

    // Total of all the discounts given
    public double getTotalDiscount() {
        double total = 0;
        for (Sale sale : sales) {
            total = total + sale.getDiscountAmount();
        }
        return total;
    }

    // Number of sales recorded
    public int getCount() {
        return sales.size();
    }

    // Sorted report: we make a COPY of the list so the original order is not changed,
    // then we sort it with our comparator (charge from highest to lowest).
    public ArrayList<Sale> getSortedReport() {
        ArrayList<Sale> copy = new ArrayList<>(sales);
        Collections.sort(copy, new ChargeComparator());
        return copy;
    }
}
