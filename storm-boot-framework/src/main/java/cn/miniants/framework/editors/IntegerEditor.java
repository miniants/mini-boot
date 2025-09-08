
package cn.miniants.framework.editors;

public class IntegerEditor extends NumberEditor {

    public IntegerEditor() {
        // to do nothing
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        this.setValue(text == null ? null : Integer.decode(text));
    }
}
