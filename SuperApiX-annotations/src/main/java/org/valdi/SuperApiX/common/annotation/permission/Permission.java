package org.valdi.SuperApiX.common.annotation.permission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Defines a plugin permission
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(Permissions.class)
public @interface Permission {
    /**
     * This permission's name.
     */
    String name();

    /**
     * This permission's description.
     */
    String desc() default "";

    /**
     * This permission's default {@link PermissionDefault}
     */
    DefaultPermission defaultValue() default DefaultPermission.OP;

    /**
     * This permission's child nodes ( {@link ChildPermission} )
     */
    ChildPermission[] children() default {};
    

	public static enum DefaultPermission {
	    TRUE("true"),
	    FALSE("false"),
	    OP("op"),
	    NOT_OP("notop");
		
		private final String value;
		
		private DefaultPermission(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return this.value;
		}
		
		@Override
		public String toString() {
			return this.value;
		}
	}
}
