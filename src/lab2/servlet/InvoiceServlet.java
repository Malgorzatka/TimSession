package lab2.servlet;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import lab2.logic.Invoice;
import lab2.logic.product.ProductPOJO;

/**
 * Created by Michal on 2017-05-07.
 */
@WebServlet(name = "InvoiceServlet", urlPatterns = "/InvoiceServlet")
public class InvoiceServlet extends HttpServlet {

    private final String INVICE_BEAN_KEY = "invoiceBean";

    //protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {}

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Invoice invoice;
        try {
            invoice = getSession(request);
        } catch (NamingException e) {
            e.printStackTrace();
            return;
        }
        //sprawdzenie czy faktura jestr otwarat
        if (invoice.getState() == Invoice.State.CLOSED ) {
            response.setStatus(409);
            response.getWriter().write("Faktura zamknięta"+invoice.getState().toString() +" "+ invoice.hashCode());
            return;
        }
        //response.getWriter().append(invoice.getState().toString() +" "+ invoice.hashCode());
        //sprawdzenie acion type

        String actionType = request.getParameter("actionType");
        int numberOfParameters =  request.getParameterMap().size();

        //ProductPOJO prod = new ProductPOJO();

        if(actionType == null || numberOfParameters == 0)
            {
                response.setStatus(400);
                response.getWriter().append(" podaj parametry");
                response.getWriter().append(invoice.getState().toString() +" "+ invoice.hashCode());
                return;
            }

            switch(actionType)
            {
                case "addProduct":
                    response.getWriter().append(invoice.getState().toString() +" "+ invoice.hashCode());
                    String productCode = checkParameter(request,"productCode", response );//request.getParameter("productCode");
                    if(productCode == null) return;

                    String name = checkParameter(request, "name", response);
                    if (name == null) return;

                    String price = checkParameter(request, "price", response);
                    if (price == null) return;
                    Double priceValue = Double.valueOf(price);
                    if(priceValue < 0.0) {
                        response.getWriter().append(" price ujemne");
                        response.setStatus(400);
                        return;
                    }

                    String quantity = checkParameter(request, "quantity", response);
                    if (quantity == null) return;
                    Integer quantityValue = Integer.valueOf(quantity);
                    if(quantityValue < 0) {
                        response.getWriter().append(" quantity ujemne");
                        response.setStatus(400);
                        return;
                    }
                    ProductPOJO newProduct = new ProductPOJO(productCode, name, priceValue);

                    invoice.addProduct(newProduct ,quantityValue);

                    response.setStatus(200);

                    response.getWriter().append("\nadd product " + newProduct + "\n" + invoice.getProducts() );
                    break;

                case "removeProduct":
                    response.getWriter().append(invoice.getState().toString() +" "+ invoice.hashCode());
                    productCode = checkParameter(request,"productCode", response );//request.getParameter("productCode");
                    if(productCode == null) return;

                    name = checkParameter(request, "name", response);
                    if (name == null) return;

                    price = checkParameter(request, "price", response);
                    if (price == null) return;
                    priceValue = Double.valueOf(price);
                    if(priceValue < 0.0) {
                        response.getWriter().write(" price ujemne");
                        response.setStatus(400);
                        return;
                    }

                    quantity = checkParameter(request, "quantity", response);
                    if (quantity == null) return;
                    quantityValue = Integer.valueOf(quantity);
                    if(quantityValue < 0) {
                        response.getWriter().write(" quantity ujemne");
                        response.setStatus(400);
                        return;
                    }
                    newProduct = new ProductPOJO(productCode, name, priceValue);

                    if(!invoice.removeProduct(newProduct, quantityValue) )
                    {
                        response.setStatus(400);
                        response.getWriter().write("\nBrak produktu na liście:");
                    }

                    response.setStatus(200);
                    response.getWriter().write("\n" + newProduct + "\n" + invoice.getProducts() );
                    break;
                case "showInvoice":

                    break;
                case "closeInvoice":
                    invoice.closeInvoice();
                    response.setStatus(200);
                    response.getWriter().append("\nZamknięto sesje" + invoice.hashCode() + "\n" + invoice.getProducts() );
                    break;

                default:

                    response.setStatus(400);
                    response.getWriter().write("bad action type in post");
            }




    }

    private Invoice getSession(HttpServletRequest request) throws ServletException, IOException, NamingException
    {
        Invoice invoice = (Invoice) request.getSession().getAttribute(INVICE_BEAN_KEY);

        if(invoice == null) {
            invoice = beanLookup();
            request.getSession().setAttribute(INVICE_BEAN_KEY , invoice);
        }
        return invoice;
    }

    private Invoice beanLookup() throws NamingException {
        return  (Invoice) new InitialContext().lookup( "java:app/lab2/Invoice" );
    }

    private String checkParameter(HttpServletRequest request, String value, HttpServletResponse response) throws IOException
    {
        String check = request.getParameter(value);
        if(check == null) {
            response.setStatus(400);

            response.getWriter().append(" podaj "+value);
           return null;
        }
        return check;
    }
}
