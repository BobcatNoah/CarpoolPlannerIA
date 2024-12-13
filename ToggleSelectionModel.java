import javax.swing.*;

// https://forums.codeguru.com/showthread.php?479357-JList-click-on-selected-item-to-deselect-it-possible
public class ToggleSelectionModel extends DefaultListSelectionModel {
    private boolean gestureStarted = false;

    public void setSelectionInterval(int index0, int index1) {
        if (gestureStarted) {
            addSelectionInterval(index0, index1);
        } else {
            if (isSelectedIndex(index0)) {
                removeSelectionInterval(index0, index1);
            } else {
                addSelectionInterval(index0, index1);
            }
        }
        gestureStarted = true;
    }

    @Override
    public void setValueIsAdjusting(boolean isAdjusting) {
        if (!isAdjusting) {
            gestureStarted = false;
        }
    }
}
