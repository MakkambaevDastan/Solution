import javax.swing.JPanel;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.util.List;

class Canvas extends JPanel {
    private Model model;
    private DComboBox box;
    private Font font;

    public Canvas(Model model) {
        this.model = model;
        font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0].deriveFont(30f);
        box = new DComboBox(this);
        box.set(List.of("Level 1", "Level 2", "Level 3", "Level 4", "Level 5", "Level 6", "Level 7", "Level 8", "Level 9", "Level 10",
                "Level 11", "Level 12", "Level 13", "Level 14", "Level 15", "Level 16", "Level 17", "Level 18", "Level 19 Makkambaev Dastan", "Level 20"));
    }

    public void paint(Graphics graphics) {
        super.paint(graphics);
        box.draw(graphics);
    }
    public void mouseDragged(int x, int y){
        box.mouseDragged(x, y);
    }
    public void mouseWheelMoved(int wheelRotation, int x, int y) {
        box.mouseWheelMoved(wheelRotation,x,y);
    }

    public void mouseMoved(int x, int y) {
        box.mouseMoved(x, y);
    }

    public void mouseClicked(int button, int x, int y) {
        box.mouseClicked(button, x, y);
    }

    public void mouseReleased(int button, int x, int y) {
        box.mouseReleased(button, x, y);
    }

    public void mousePressed(int button, int x, int y) {
        box.mousePressed(button, x, y);
    }
}
