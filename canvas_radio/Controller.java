import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class Controller implements MouseMotionListener, MouseListener, MouseWheelListener {
    private Model model;

    public Controller(Viewer viewer) {
        model = new Model(viewer);
    }

    public Model getModel() {
        return model;
    }

    // MouseMotionListener
    public void mouseDragged(MouseEvent event) {
        model.mouseDragged(event.getX(), event.getY());
//        System.out.println("mouseDragged " + event.getX() + "," + event.getY());
    }

    // MouseMotionListener
    public void mouseMoved(MouseEvent event) {
        model.mouseMoved(event.getX(), event.getY());
//        System.out.println("mouseMoved "+event.getX() + "," + event.getY());
    }

    // MouseListener
    public void mouseClicked(MouseEvent event) {
        model.mouseClicked(event.getButton(), event.getX(), event.getY());
//        System.out.println("mouseClicked "+event.getButton()+" "+event.getX() + "," + event.getY());
    }

    // MouseListener
    public void mousePressed(MouseEvent event) {
        model.mousePressed(event.getButton(), event.getX(), event.getY());
//        System.out.println("mousePressed " + event.getButton() + " " + event.getX() + " " + event.getY());
    }

    // MouseListener
    public void mouseReleased(MouseEvent event) {
        model.mouseReleased(event.getButton(), event.getX(), event.getY());
//        System.out.println("mouseReleased " + event.getButton() + " " + event.getX() + " " + event.getY());
    }

    // MouseListener
    public void mouseEntered(MouseEvent event) {
//        model.mouseEntered(event.getButton(), event.getX(), event.getY());
//        System.out.println("mouseEntered " + event.getButton() + " " + event.getX() + " " + event.getY());
    }

    // MouseListener
    public void mouseExited(MouseEvent event) {
    }

    // MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent event) {
        model.mouseWheelMoved(event.getWheelRotation(), event.getX(), event.getY());
//        System.out.println("mouseWheelMoved " + event.getWheelRotation() + " " + event.getX() + " " + event.getY());
    }
}
