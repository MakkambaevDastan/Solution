import java.util.Random;

public class Affect implements Runnable {
    private Model model;
    private Thread thread;
    private Random random;

    public Affect(Model model) {
        this.model = model;
        thread = new Thread(this);
        random = new Random();
    }

    public void run() {
        while (true) {
            int ran = random.nextInt();
            model.setState(ran);
            System.out.println(ran);
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ie) {
                System.out.println(ie);
            }
        }
    }

    public void go() {
        thread.start();
    }
}
