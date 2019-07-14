package org.valdi.SuperApiX.bukkit.nms.core;

public class VersionUnsupportedException extends RuntimeException {
	
    private static final long serialVersionUID = -7220124061402078571L;


    public VersionUnsupportedException() {
        super();
    }
    
    public VersionUnsupportedException(Exception e) {
        super(e);
    }
    
    public VersionUnsupportedException(String message) {
        super(message);
    }
    
    public VersionUnsupportedException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

