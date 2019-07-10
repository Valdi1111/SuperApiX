package org.valdi.SuperApiX.common.databases.advanced;

/**
 * The Callback interface is designed to allow for a common, reusable interface
 * to exist for defining APIs that requires a call back in certain situations.
 * <p>
 * Callback is defined with one generic parameters which
 * specifies the type of the object passed in to the <code>call</code> method.
 *
 * @param <Parameter> The type of the argument provided to the <code>call</code> method.
 * @since 3.5
 */
@FunctionalInterface
public interface Callback<Parameter> {
    /**
     * The <code>call</code> method is called when required, and is given a
     * single argument of type Parameter.
     *
     * @param param The single argument upon which the returned value should be
     *      determined.
     */
    void call(Parameter param);
}
