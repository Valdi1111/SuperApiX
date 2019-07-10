package org.valdi.SuperApiX.common.scheduler.task;

import org.valdi.SuperApiX.common.plugin.StoreLoader;

public class SuperAsyncDebugger {
    private SuperAsyncDebugger next = null;
    private final int expiry;
    private final StoreLoader plugin;
    private final Class<?> clazz;

    public SuperAsyncDebugger(int expiry, StoreLoader plugin, Class<?> clazz) {
        this.expiry = expiry;
        this.plugin = plugin;
        this.clazz = clazz;
    }

    public final SuperAsyncDebugger getNextHead(int time) {
        SuperAsyncDebugger next;
        SuperAsyncDebugger current = this;
        while (time > current.expiry && (next = current.next) != null) {
            current = next;
        }
        return current;
    }

    public final SuperAsyncDebugger setNext(SuperAsyncDebugger next) {
        this.next = next;
        return this.next;
    }

    public StringBuilder debugTo(StringBuilder string) {
        SuperAsyncDebugger next = this;
        while (next != null) {
            string.append(next.plugin.getName()).append(':').append(next.clazz.getName()).append('@').append(next.expiry).append(',');
            next = next.next;
        }
        return string;
    }
}
