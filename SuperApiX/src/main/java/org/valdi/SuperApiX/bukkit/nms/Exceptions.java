package org.valdi.SuperApiX.bukkit.nms;

public class Exceptions {

	public static final ExceptionType FALLBACK_CLASS_NOT_FOUND = new ExceptionType("Cannot find an usable class... Please contact the developer.");
	public static final ExceptionType NOT_PRESENT = new ExceptionType("Method not found in this nms version");
	public static final ExceptionType UNSUPPORTED_NMS = new ExceptionType("Nms not supported");
	public static final ExceptionType UNSUPPORTED_SPIGOT = new ExceptionType("Spigot not supported");
	
	public static class ExceptionType {
		
		private final String explenation;
		
		public ExceptionType(final String explenation) {
			this.explenation = explenation;
		}
		
		public String getExplenation() {
			return this.explenation;
		}
		
	}

}
