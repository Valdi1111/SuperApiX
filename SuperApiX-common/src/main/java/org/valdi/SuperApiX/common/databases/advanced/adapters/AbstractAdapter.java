package org.valdi.SuperApiX.common.databases.advanced.adapters;

public abstract class AbstractAdapter<Type> implements ColumnAdapter<Type> {
    private final Class<Type> clazz;

    public AbstractAdapter(Class<Type> clazz) {
        this.clazz = clazz;
    }

    /**
     * Get the class of this serializer.
     * @return the class
     */
    public Class<Type> getClazz() {
        return clazz;
    }

}
