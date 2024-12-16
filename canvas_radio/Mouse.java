import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

class Mouse implements MouseMotionListener, MouseListener, MouseWheelListener {
    private DComboBox box;

    public Mouse(DComboBox box) {
        this.box = box;
    }

    // MouseMotionListener
    public void mouseDragged(MouseEvent event) {
    }

    // MouseMotionListener
    public void mouseMoved(MouseEvent event) {
        box.mouseMoved(event.getX(), event.getY());
        System.out.println("mouseMoved " + event.getX() + "," + event.getY());
    }

    // MouseListener
    public void mouseClicked(MouseEvent event) {
        box.mouseClicked(event.getButton(), event.getX(), event.getY());
        System.out.println("mouseClicked " + event.getButton() + " " + event.getX() + "," + event.getY());
    }

    // MouseListener
    public void mousePressed(MouseEvent event) {
    }

    // MouseListener
    public void mouseReleased(MouseEvent event) {
    }

    // MouseListener
    public void mouseEntered(MouseEvent event) {
    }

    // MouseListener
    public void mouseExited(MouseEvent event) {
    }

    // MouseWheelListener
    public void mouseWheelMoved(MouseWheelEvent event) {
        box.mouseWheelMoved(event.getWheelRotation(), event.getX(), event.getY());
        System.out.println("mouseWheelMoved " + event.getWheelRotation());
    }
}
