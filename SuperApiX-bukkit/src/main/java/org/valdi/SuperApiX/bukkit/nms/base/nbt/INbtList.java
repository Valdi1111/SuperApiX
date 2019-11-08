package org.valdi.SuperApiX.bukkit.nms.base.nbt;

import java.util.*;

public interface INbtList<T> extends List<T> {

	/**
	 * @return The NMS Compound behind this Object
	 */
	Object getHandle();

	@Override
	boolean add(T element);

	@Override
	void add(int index, T element);

	@Override
	T get(int index);

	@Override
	T set(int index, T element);

	T remove(int i);

	int size();

	/**
	 * @return The type that this list contains
	 */
	NBTType getType();

	@Override
	boolean isEmpty();

	@Override
	void clear();

	@Override
	boolean contains(Object o);

	@Override
	int indexOf(Object o);

	@Override
	boolean addAll(Collection<? extends T> c);

	@Override
	boolean addAll(int index, Collection<? extends T> c);

	@Override
	boolean containsAll(Collection<?> c);

	@Override
	int lastIndexOf(Object o);

	@Override
	boolean removeAll(Collection<?> c);

	@Override
	boolean retainAll(Collection<?> c);

	@Override
	boolean remove(Object o);

	@Override
	Iterator<T> iterator();

	@Override
	ListIterator<T> listIterator();

	@Override
	ListIterator<T> listIterator(int startIndex);

	@Override
	Object[] toArray();

	@Override
	<E> E[] toArray(E[] a);

	@Override
	List<T> subList(int fromIndex, int toIndex);

}
