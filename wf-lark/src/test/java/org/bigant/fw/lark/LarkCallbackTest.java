package org.bigant.fw.lark;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author galen
 * @date 2024/3/1216:31
 */
public class LarkCallbackTest extends BaseTest {

    @Test
    public void test() throws Exception {
        LarkCallback larkCallback = new LarkCallback(null, verificationToken, encryptKey);

        larkCallback.callback("1647192413", "1647192413", "1647192413", "encrypt -> PwLHKCQtROKSV3moNcpcLwXLtcfHBL6GfBSkASEz1f8fxDKAkbiGuo2HBnsfdv+sSn6LFPQ+z+XZ0Rt1cdyMZg0ktUckBTV6tcYrw+FU71Zr5CrT6G3RS3gQ7eiy9CxF92MHvm7MdwTb4XnfPcnf571B83wv+o7Ft/Zg7KiXrg/HFRXVsXiCDXbdf27rHIbB");
    }

}