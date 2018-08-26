package org.valdi.SuperApiX.common.dependencies.relocation;

import java.util.Arrays;
import java.util.List;

public final class Relocation {
    private static final String RELOCATION_PREFIX = "org.valdi.SuperApiX.lib.";

    public static Relocation of(String id, String pattern) {
        return new Relocation(pattern.replace("{}", "."), RELOCATION_PREFIX + id);
    }

    public static List<Relocation> allOf(Relocation... relocations) {
        return Arrays.asList(relocations);
    }

    private final String pattern;
    private final String relocatedPattern;

    private Relocation(String pattern, String relocatedPattern) {
        this.pattern = pattern;
        this.relocatedPattern = relocatedPattern;
    }

    public String getPattern() {
        return this.pattern;
    }

    public String getRelocatedPattern() {
        return this.relocatedPattern;
    }

}
