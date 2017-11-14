
package org.eclipse.ceylon.tools.copy;

import java.util.ResourceBundle;


class CeylonCopyMessages extends org.eclipse.ceylon.common.Messages {

    public static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(CeylonCopyMessages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}