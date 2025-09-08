
package cn.miniants.framework.editors;

public class DoubleEditor extends NumberEditor {

    public DoubleEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Double.valueOf(text));
    }
}
