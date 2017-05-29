package lab2.logic;

import lab2.logic.product.ProductPOJO;

import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Malgorzata on 2017-05-28.
 */
@Stateful(name = "Invoice")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
public class InvoiceBean implements Invoice {

    private State beanState;
    Map<ProductPOJO, Integer> MapProduct = new HashMap<ProductPOJO, Integer>();


    @PostConstruct
    private void init() {
        this.beanState = State.OPEN;


    }

    @Override
    public void addProduct(ProductPOJO productPOJO, int quantity) {
        ProductPOJO prod = productInList(productPOJO);
        if(prod == null)
        {
            MapProduct.put(productPOJO, quantity);
        }else
        {
            MapProduct.put(prod, MapProduct.get(prod) + quantity);
        }
    }

    @Override
    public boolean removeProduct(ProductPOJO productPOJO, int quantity)  {
        ProductPOJO prod = productInList(productPOJO);
        if(prod == null)
        {
            return false;
        }else
        {
            MapProduct.put(prod, MapProduct.get(prod) - quantity);
            if(MapProduct.get(prod) <= 0)
            {
                MapProduct.remove(prod);
            }
        }
        return true;
    }

    @Override
    public Map<ProductPOJO, Integer> getProducts() {
        return MapProduct;
    }

    @Override
    public double calculateInvoice() {
        return 0;
    }

    @Override
    public double calculateTax() {
        return 0;
    }

    @Override
    public void closeInvoice() {
        this.beanState = State.CLOSED;
    }

    @Override
    public State getState() {
        return beanState;
    }

    //sprawdzenie czy produkt jest na liscie
    private ProductPOJO productInList (ProductPOJO newProduct)
    {
        //int index = 0;
        //if(MapProduct.size() == 0) return -1;
        Iterator iterator = MapProduct.keySet().iterator();
        ProductPOJO product;// = (ProductPOJO) iterator.next();//.get(index);
        while (iterator.hasNext() )
        {
            product = (ProductPOJO) iterator.next();
            if(product.getProductCode().equals(newProduct.getProductCode()) && product.getName().equals(newProduct.getName()) && (Double.compare(product.getPrice(),newProduct.getPrice()) == 0) )
            {
                return product;
            }

            //index++;
           // product = products.get(index);
        }
        return null;
    }

}
