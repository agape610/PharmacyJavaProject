import java.util.Comparator;

public class ChargeComparator implements Comparator<Sale> {

    @Override
    public int compare(Sale first, Sale second) {
        // We put "second" first in Double.compare to reverse the order:
        return Double.compare(second.calculateCharge(), first.calculateCharge());
    }
}
