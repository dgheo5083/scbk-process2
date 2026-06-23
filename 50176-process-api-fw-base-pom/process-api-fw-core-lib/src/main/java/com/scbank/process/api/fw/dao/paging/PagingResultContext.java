package com.scbank.process.api.fw.dao.paging;

public class PagingResultContext {

    private static ThreadLocal<Boolean> hasNextHolder = new ThreadLocal<>();

    public static void setHasNext(boolean hasNext) {
        hasNextHolder.set(hasNext);
    }

    public static Boolean getHasNext() {
        return hasNextHolder.get();
    }

    public static void clear() {
        hasNextHolder.remove();
    }
}
