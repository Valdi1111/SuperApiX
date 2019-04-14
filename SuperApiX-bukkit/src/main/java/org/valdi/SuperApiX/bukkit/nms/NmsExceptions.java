package org.valdi.SuperApiX.bukkit.nms;

public class NmsExceptions {

	public static final NmsException FALLBACK_CLASS_NOT_FOUND = new NmsException("Cannot find an usable class... Please contact the developer.");
	public static final NmsException NOT_PRESENT = new NmsException("Method not found in this nms version");
	public static final NmsException UNSUPPORTED_NMS = new NmsException("Nms not supported");
	public static final NmsException UNSUPPORTED_SPIGOT = new NmsException("Spigot not supported");
	
	public static class NmsException {
		
		private final String explenation;
		
		public NmsException(final String explenation) {
			this.explenation = explenation;
		}
		
		public String getExplenation() {
			return this.explenation;
		}
		
	}

}
