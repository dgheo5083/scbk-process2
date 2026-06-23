package com.scbank.process.api.fw.core.concurrent.lock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * 읽기/쓰기 동기화를 위한 공통 락 지원 클래스
 */
public abstract class ReadWriteLockSupport {

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    protected void withWrite(Runnable task) {
        lock.writeLock().lock();
        try {
            task.run();
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected <T> T withWrite(Supplier<T> task) {
        lock.writeLock().lock();
        try {
            return task.get();
        } finally {
            lock.writeLock().unlock();
        }
    }

    protected void withRead(Runnable task) {
        lock.readLock().lock();
        try {
            task.run();
        } finally {
            lock.readLock().unlock();
        }
    }

    protected <T> T withRead(Supplier<T> task) {
        lock.readLock().lock();
        try {
            return task.get();
        } finally {
            lock.readLock().unlock();
        }
    }

    protected int withReadInt(IntSupplier supplier) {
        lock.readLock().lock();
        try {
            return supplier.getAsInt();
        } finally {
            lock.readLock().unlock();
        }
    }
}
