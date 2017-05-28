package lab2.example.servlet;

import lab2.example.ejb.Foo;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



@WebServlet(name = "ExampleServlet", urlPatterns = "/ExampleServlet")
public class ExampleServlet extends HttpServlet {

    private final String FOO_BEAN_KEY = "fooBean";

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            Foo foo = getSession(request);
            
            foo.changeState();
        } catch (NamingException e) {
            throw new ServletException(e);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Foo foo = getSession(request);

            response.getWriter().append(foo.getSatate() + foo.hashCode());
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }

    private Foo getSession(HttpServletRequest request) throws ServletException, IOException, NamingException
    {
        Foo foo = (Foo) request.getSession().getAttribute(FOO_BEAN_KEY);

        if(foo == null) {
            foo = beanLookup();
            request.getSession().setAttribute(FOO_BEAN_KEY , foo);
        }
        return foo;
    }

    private Foo beanLookup() throws NamingException {
        return  (Foo) new InitialContext().lookup( "java:app/lab2/FooEJB" );
    }
}
