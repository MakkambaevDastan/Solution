import java.awt.FontMetrics;
import java.awt.GraphicsEnvironment;
import java.awt.Polygon;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class DComboBox {
    private int x;
    private int y;
    private int width;
    private int height;
    private int textOffset;
    private float fontSize;
    private int viewCount;
    private int scrollBarWidth;
    private int scrollBarHeight;
    private int scrollBarOffset;
    private int scrollBarPercentage;
    private boolean scrollBarDragged;
    private boolean slider;
    private int startIndex;
    private int focusIndexList;
    private int focusIndexCount;
    private int listSize;
    private Polygon icon;
    private List<String> list;
    private List<String> listDraw;
    private int selectedKey;
    private String labelDefault;
    private String labelSelected;
    private Font font;
    private FontMetrics fontMetrics;
    private Color backgroundColor;
    private Color selectedColor;
    private Color focusColor;
    private Color scrollBarColor;
    private Color scrollBarSpaceColor;
    private boolean open;
    private Graphics graphics;
    private Canvas parent;

    public DComboBox(Canvas parent) {
        this.parent = parent;
        x = 100;
        y = 100;
        viewCount = 5;
        scrollBarWidth = 15;
        list = new ArrayList<>();
        listDraw = new ArrayList<>();
        labelDefault = "Levels";
        labelSelected = labelDefault;
        backgroundColor = Color.WHITE;
        selectedColor = new Color(50, 148, 247);
        focusColor = new Color(100, 200, 200);
        scrollBarColor = new Color(197, 214, 230);
        scrollBarSpaceColor = new Color(240, 240, 240);

        selectedKey = -1;
        startIndex = selectedKey;
        fontSize = 16f;
        font = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()[0].deriveFont(fontSize);
        fontMetrics = Toolkit.getDefaultToolkit().getFontMetrics(font);
        width = 150;
        height = 30;
        double p = ((double) height / 100);
        textOffset = (int) (p * 70);
        int[] iconX = new int[]{
                x + width + (int) (p * 30) - height,
                x + width + (int) (p * 60) - height,
                x + width + (int) (p * 45) - height
        };
        int[] iconY = new int[]{
                y + (int) (p * 30),
                y + (int) (p * 30),
                y + (int) (p * 60)
        };
        icon = new Polygon(iconX, iconY, 3);
    }

    public void draw(Graphics graphics) {
        if (this.graphics == null || this.graphics != graphics) {
            this.graphics = graphics;
        }
        graphics.setFont(font);
        drawButton(graphics);
        if (open) {
            drawListRectangle(graphics);
        }
    }

    public void set(List<String> list) {
        this.list = list;
        if (!listDraw.isEmpty()) {
            listDraw.clear();
        }
        listDraw.addAll(list);
        listSize = list.size();
        int stringWidth = fontMetrics.stringWidth(labelSelected);
        int maxChar = (labelSelected.length() * (width - scrollBarWidth) / stringWidth);
        for (int i = 0; i < listSize; i++) {
            stringWidth = fontMetrics.stringWidth(list.get(i));
            if (stringWidth > width - scrollBarWidth) {
                String resultString = list.get(i);
                resultString = resultString.substring(0, maxChar - 4);
                resultString = resultString.concat("...");
                listDraw.set(i, resultString);
            }
        }
        calculateScrollBarHeight();
    }

    private void calculateScrollBarHeight() {
        startIndex = listSize > (startIndex + viewCount) ? selectedKey - (viewCount / 2) : listSize - viewCount;
        if (list.size() > viewCount) {
            scrollBarHeight = (int) ((double) (height * viewCount) / 100 * (100 / ((double) list.size() / viewCount)));
        } else {
            viewCount = list.size();
            scrollBarHeight = 0;
        }
    }

    private void drawButton(Graphics graphics) {
        graphics.setColor(selectedColor);
        graphics.fillRect(x, y, width, height);
        graphics.setColor(Color.WHITE);
        graphics.drawRect(x, y, width, height);
        graphics.fillPolygon(icon);
        graphics.drawString(labelSelected, x + 10, y + textOffset);
    }

    private void drawListRectangle(Graphics graphics) {
        drawRectangle(graphics);
        drawList(graphics);
    }

    private void drawRectangle(Graphics graphics) {
        graphics.setColor(backgroundColor);
        graphics.fillRect(x, y + height + 5, width - scrollBarWidth, height * viewCount);
        graphics.setColor(Color.BLACK);
        graphics.drawRect(x, y + height + 5, width - scrollBarWidth, height * viewCount);
    }

    private void drawList(Graphics graphics) {
        startIndex = (listSize < startIndex + viewCount) ? listSize - viewCount : startIndex < 0 ? 0 : startIndex;
        for (int i = startIndex, j = 1; j <= viewCount; j++, i++) {
            if (i == selectedKey) {
                graphics.setColor(selectedColor);
                graphics.fillRect(x + 1, y + height * j + 6, width - scrollBarWidth - 1, height);
                graphics.setColor(Color.WHITE);
                graphics.drawString(listDraw.get(i), x + 10, y + textOffset + (height * j));
                graphics.setColor(Color.BLACK);
            } else if (i < listSize) {
                if (j == focusIndexCount) {
                    graphics.setColor(focusColor);
                    graphics.fillRect(x + 1, y + height * j + 6, width - scrollBarWidth - 1, height);
                    graphics.setColor(Color.BLACK);
                }
                graphics.drawString(listDraw.get(i), x + 10, y + textOffset + (height * j));
            }
        }
        drawScrollBar(graphics);
    }

    private void drawScrollBar(Graphics graphics) {
        if (scrollBarHeight > 0) {
            scrollBarPercentage = (int) (100.0 / ((listSize - 2 * ((double) viewCount / 2.0)) / startIndex));
            scrollBarOffset = (int) (((double) (viewCount * height - scrollBarHeight) / 100.0) * scrollBarPercentage);

            graphics.setColor(scrollBarSpaceColor);
            graphics.fillRect(x + width - scrollBarWidth, y + height + 5, scrollBarWidth, scrollBarOffset);
            graphics.fillRect(x + width - scrollBarWidth, y + height + 5 + scrollBarHeight + scrollBarOffset, scrollBarWidth,
                    (viewCount * height) - (scrollBarHeight + scrollBarOffset));

            graphics.setColor(scrollBarColor);
            graphics.fillRect(x + width - scrollBarWidth, y + height + 5 + scrollBarOffset, scrollBarWidth, scrollBarHeight);

            graphics.setColor(Color.BLACK);
            graphics.drawRect(x + width - scrollBarWidth, y + height + 5 + scrollBarOffset, scrollBarWidth, scrollBarHeight);
            graphics.drawRect(x + width - scrollBarWidth, y + height + 5, scrollBarWidth, scrollBarOffset);
            graphics.drawRect(x + width - scrollBarWidth, y + height + 5 + scrollBarHeight + scrollBarOffset, scrollBarWidth,
                    (viewCount * height) - (scrollBarHeight + scrollBarOffset));
        }
    }

    private void drawFocus(int x, int y) {
        focusIndexCount = (y - this.y + 5) / height;
        if (slider) {
            startIndex = (int) (((double) listSize / 100.0) * (100.0 / ((double) (viewCount * height) / ((y - this.y) - height)))); // scroll
        }
        focusIndexList = startIndex + (focusIndexCount - 1);
        graphics.setColor(focusColor);
        graphics.fillRect(x + 1, y + height * focusIndexCount + 6, width - scrollBarWidth - 1, height);
        parent.repaint();
    }

    public void mouseDragged(int x, int y) {
        if (open && !scrollBarDragged) {
            scrollBarPressed(x, y);
        } else if (scrollBarDragged && mouseInsideListHeight(x, y)) {
            startIndex = (int) (((double) listSize / 100.0) * (100.0 / ((double) (viewCount * height) / ((y - this.y) - height)))); // scroll
            drawFocus(x, y);
        }
    }

    private boolean mouseInsideListHeight(int x, int y) {
        return this.y + height < y && y < this.y + (height * (viewCount + 1));
    }

    private void scrollBarPressed(int x, int y) {
        if ((this.x + width - scrollBarWidth) < x && x < (this.x + width) &&
                this.y + height + 5 < y && y < this.y + height + 5 + height * viewCount) {
            startIndex = (int) (((double) listSize / 100.0) * (100.0 / ((double) (viewCount * height) / ((y - this.y) - height)))); // scroll
            scrollBarDragged = true;
            drawFocus(x, y);
        }
    }

    public void mouseWheelMoved(int wheelRotation, int x, int y) {
        if (open && mouseInRectangle(x, y)) {
            startIndex = ((startIndex + wheelRotation) < 0) ? 0 :
                    (listSize < (startIndex + wheelRotation) + viewCount) ? listSize - viewCount : (startIndex + wheelRotation);
            focusIndexList = startIndex + (focusIndexCount - 1);
            parent.repaint();
        }
    }

    private boolean mouseInRectangle(int x, int y) {
        return this.x + 5 < x && x < (this.x - 5 + width) && this.y + 5 + height < y && y < this.y + (height * (viewCount + 1));
    }

    public void mouseMoved(int x, int y) {
        if (open && mouseInRectangle(x, y)) {
            drawFocus(x, y);
        }
    }

    public void mouseClicked(int button, int x, int y) {
        if (open && button == 1 && mouseInRectangleList(x, y)) {
            selectedKey = focusIndexList;
            labelSelected = listDraw.get(selectedKey);
            open = false;
        } else if (open && button == 3 && mouseInRectangleList(x, y) && selectedKey == focusIndexList) {
            selectedKey = -1;
            labelSelected = labelDefault;
        }
        showList(button, x, y);
    }

    private void showList(int button, int x, int y) {
        if (!open && button == 1 && mouseInRectangleButton(x, y)) {
            open = true;
            if (selectedKey > -1) {
                startIndex = selectedKey - viewCount / 2;
            }
        } else if(open && button == 1 ) {
            open = false;
        }
        parent.repaint();
    }

    private boolean mouseInRectangleList(int x, int y) {
        return this.x < x && x < (this.x + width - scrollBarWidth) && this.y + height + 10 < y && y < this.y + 5 + (height * (viewCount + 1));
    }

    private boolean mouseInRectangleButton(int x, int y) {
        return this.x - 5 < x && x < (this.x - 5 + width) && this.y - 5 < y && y < this.y - 5 + height;
    }

    public void mouseReleased(int button, int x, int y) {
        if (open && button == 1) {
            scrollBarDragged = false;
        }
    }

    public void mousePressed(int button, int x, int y) {
        if (open && button == 1) {
            scrollBarPressed(x, y);
        }
    }
}