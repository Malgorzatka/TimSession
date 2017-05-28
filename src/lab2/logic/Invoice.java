package lab2.logic;

import lab2.logic.product.ProductPOJO;

import javax.ejb.Local;
import java.util.Map;

@Local
public interface Invoice {

    enum State {OPEN , CLOSED}

    void addProduct(ProductPOJO productPOJO, int quantity);

    void removeProduct(ProductPOJO productPOJO, int quantity);

    Map<ProductPOJO , Integer> getProducts();

    double calculateInvoice();

    double calculateTax();

    void closeInvoice();

    State getState();

}
