
package cn.miniants.framework.editors;

import java.beans.PropertyEditorSupport;

public class NumberEditor extends PropertyEditorSupport {

    public NumberEditor() {
        // to do nothing
    }

    @Override
    public String getJavaInitializationString() {
        Object var1 = this.getValue();
        return var1 != null ? var1.toString() : "null";
    }
}
