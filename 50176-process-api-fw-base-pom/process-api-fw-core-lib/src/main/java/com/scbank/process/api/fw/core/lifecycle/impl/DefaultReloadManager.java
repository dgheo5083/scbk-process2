package com.scbank.process.api.fw.core.lifecycle.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.scbank.process.api.fw.core.lifecycle.IReloadManager;
import com.scbank.process.api.fw.core.lifecycle.IReloadable;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DefaultReloadManager implements IReloadManager {

    private final List<IReloadable> reloadables;

    @Override
    public List<IReloadable> getReloadables() {
        return reloadables;
    }

    @Override
    public void reload() {
        if (CollectionUtils.isEmpty(reloadables)) {
            return;
        }

        for (IReloadable reloadable : reloadables) {
            safeReload(reloadable);
        }
    }

    @Override
    public void reload(Class<? extends IReloadable> type) {
        for (IReloadable reloadable : reloadables) {
            if (type.isAssignableFrom(reloadable.getClass())) {
                safeReload(reloadable);
            }
        }
    }

    @Override
    public void reload(String simpleClassName) {
        for (IReloadable reloadable : reloadables) {
            if (reloadable.getClass().getSimpleName().equalsIgnoreCase(simpleClassName)) {
                safeReload(reloadable);
            }
        }
    }

    private void safeReload(IReloadable reloadable) {
        try {
            reloadable.reload();
            log.info("[Reload] Success: {}", reloadable.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("[Reload] Failed: " + reloadable.getClass().getSimpleName(), e);
        }
    }
}
