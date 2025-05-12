package gui.model;

public class GUIModel {
    private boolean dataChanged = false;

    public boolean isDataChanged() {
        return dataChanged;
    }

    public void setDataChanged(boolean dataChanged) {
        this.dataChanged = dataChanged;
    }
}
