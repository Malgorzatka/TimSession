package lab2.example.ejb;


import javax.ejb.Local;

@Local
public interface Foo {

    void changeState();

    String getSatate();

}
