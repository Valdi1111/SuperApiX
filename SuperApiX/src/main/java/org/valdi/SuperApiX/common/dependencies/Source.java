package org.valdi.SuperApiX.common.dependencies;

import java.nio.file.Path;

public class Source {
    private final Dependency dependency;
    private final Path file;

    public Source(Dependency dependency, Path file) {
        this.dependency = dependency;
        this.file = file;
    }
    
    public Dependency getDependency() {
    	return this.dependency;
    }
    
    public Path getFile() {
    	return this.file;
    }
    
}
