public class Model {
    private Viewer viewer;
    private int state;
    private Affect affect;

    public Model(Viewer viewer) {
        this.viewer = viewer;
        affect = new Affect(this);
    }

    public Affect getAffect() {
        return affect;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
        viewer.update();
    }
    public void mouseDragged(int x, int y){
        viewer.mouseDragged(x, y);
    }
    public void mouseWheelMoved(int wheelRotation, int x, int y) {
        viewer.mouseWheelMoved(wheelRotation, x, y);
    }

    public void mouseMoved(int x, int y) {
        viewer.mouseMoved(x, y);
    }

    public void mouseClicked(int button, int x, int y) {
        viewer.mouseClicked(button, x, y);
    }

    public void mouseReleased(int button, int x, int y) {
        viewer.mouseReleased(button, x, y);
    }

    public void mousePressed(int button, int x, int y) {
        viewer.mousePressed(button, x, y);
    }
}
