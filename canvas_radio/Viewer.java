import javax.swing.JFrame;
import java.awt.Color;

public class Viewer {
    private Affect affect;
    private Canvas[] canvasContainer;

    public Viewer() {
        Controller controller = new Controller(this);
        Model model = controller.getModel();
        canvasContainer = new Canvas[4];
        canvasContainer[0] = new Canvas(model);
        canvasContainer[0].setBounds(0, 0, 500, 500);
        canvasContainer[0].setBackground(Color.YELLOW);
        canvasContainer[0].addMouseListener(controller);
        canvasContainer[0].addMouseMotionListener(controller);
        canvasContainer[0].addMouseWheelListener(controller);

//        canvasContainer[1] = new Canvas(model);
//        canvasContainer[1].setBounds(0, 100, 500, 100);
//        canvasContainer[1].setBackground(Color.GRAY);
//
//        canvasContainer[2] = new Canvas(model);
//        canvasContainer[2].setBounds(0, 200, 500, 100);
//        canvasContainer[2].setBackground(Color.GREEN);
//
//        canvasContainer[3] = new Canvas(model);
//        canvasContainer[3].setBounds(0, 300, 500, 100);
//        canvasContainer[3].setBackground(Color.CYAN);
        affect = model.getAffect();

        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(100, 100, 500, 500);
        frame.setLayout(null);
        frame.add(canvasContainer[0]);
//        frame.add(canvasContainer[1]);
//        frame.add(canvasContainer[2]);
//        frame.add(canvasContainer[3]);
        frame.setVisible(true);
    }

    public Affect getAffect() {
        return affect;
    }

    public void update() {
//        for (Canvas canvas : canvasContainer) {
//            canvas.repaint();
//        }
        canvasContainer[0].repaint();
    }
    public void mouseDragged(int x, int y){
        canvasContainer[0].mouseDragged(x, y);
    }
    public void mouseWheelMoved(int wheelRotation, int x, int y) {
        canvasContainer[0].mouseWheelMoved(wheelRotation, x, y);
//        System.out.println("mouseWheelMoved " + wheelRotation);
    }

    public void mouseMoved(int x, int y) {
        canvasContainer[0].mouseMoved(x, y);
//        System.out.println("mouseMoved " + x + "," + y);
    }

    public void mouseClicked(int button, int x, int y) {
        canvasContainer[0].mouseClicked(button, x, y);
//        System.out.println("mouseClicked " + button + " " + x + "," + y);
    }

    public void mouseReleased(int button, int x, int y) {
        canvasContainer[0].mouseReleased(button, x, y);
    }

    public void mousePressed(int button, int x, int y) {
        canvasContainer[0].mousePressed(button, x, y);
    }
}
