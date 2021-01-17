
package com.zitlab.palmyra.cinch.quirks.db2;

import com.zitlab.palmyra.cinch.quirks.Quirks;
import com.zitlab.palmyra.cinch.quirks.QuirksProvider;

public class Db2QuirksProvider implements QuirksProvider {
    @Override
    public Quirks provide() {
        return new Db2Quirks();
    }

    @Override
    public boolean isUsableForUrl(String url) {
        return url.startsWith("jdbc:db2:")
                || url.startsWith("jdbc:db2j:net:")
                || url.startsWith("jdbc:db2os390");
    }

    @Override
    public boolean isUsableForClass(String className) {
        return className.startsWith("com.ibm.db2.jcc.DB2");
    }
}
