package org.valdi.SuperApiX.bukkit.storage;

public interface IElementsTable<T> {
	
	void addElement(T element);
	
	void removeElement(T element);
	
	void saveElement(T element);

}
