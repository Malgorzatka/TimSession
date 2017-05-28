package lab2.example.ejb;


import javax.annotation.PostConstruct;
import javax.ejb.Stateful;
import javax.ejb.StatefulTimeout;
import java.util.concurrent.TimeUnit;

@Stateful(name = "FooEJB")
@StatefulTimeout(unit = TimeUnit.MINUTES, value = 30)
public class FooBean implements Foo {

    private enum State {INITIALIZED , STATE_A, STATE_B, FINISHED}

    private State beanState;

    @PostConstruct
    private void init() {
        this.beanState = State.INITIALIZED;
    }


    public void changeState() {
        switch (beanState) {
            case INITIALIZED:
                this.beanState = State.STATE_A;
                break;
                                             
            case STATE_A:
                this.beanState = State.STATE_B;
                break;

            case STATE_B:
                this.beanState = State.FINISHED;
                break;

            case FINISHED:
                this.beanState = State.INITIALIZED;
                break;
        }
    }

    public String getSatate() {
        return this.beanState.name();
    }


}
