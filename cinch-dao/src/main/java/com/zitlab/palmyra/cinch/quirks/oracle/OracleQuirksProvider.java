
package com.zitlab.palmyra.cinch.quirks.oracle;

import com.zitlab.palmyra.cinch.quirks.Quirks;
import com.zitlab.palmyra.cinch.quirks.QuirksProvider;

public class OracleQuirksProvider implements QuirksProvider {
    @Override
    public Quirks provide() {
        return new OracleQuirks();
    }

    @Override
    public boolean isUsableForUrl(String url) {
        return url.startsWith("jdbc:oracle:");
    }

    @Override
    public boolean isUsableForClass(String className) {
        return className.startsWith("oracle.jdbc.")
                || className.startsWith("oracle.jdbc.");
    }

}
