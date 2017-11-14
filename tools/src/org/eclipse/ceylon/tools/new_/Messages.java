
package org.eclipse.ceylon.tools.new_;

import java.util.ResourceBundle;


class Messages extends org.eclipse.ceylon.common.Messages {

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(Messages.class.getPackage().getName() + ".resources.messages");

    public static String msg(String msgKey, Object... msgArgs) {
        return msg(RESOURCE_BUNDLE, msgKey, msgArgs);
    }

}