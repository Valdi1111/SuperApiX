package org.valdi.SuperApiX.common.dependencies;

import java.io.File;

public class Source {
    private final Dependency dependency;
    private final File file;

    public Source(Dependency dependency, File file) {
        this.dependency = dependency;
        this.file = file;
    }
    
    public Dependency getDependency() {
    	return this.dependency;
    }
    
    public File getFile() {
    	return this.file;
    }
    
}
