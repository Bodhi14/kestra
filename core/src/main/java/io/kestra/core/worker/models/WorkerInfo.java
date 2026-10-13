package io.kestra.core.worker.models;

import io.kestra.core.utils.IdUtils;

import io.micronaut.context.annotation.Context;

/**
 * Provides information about the current worker.
 * <p>
 * Owns the worker id rather than reading it from the worker, since plugins are synchronized under this id before the worker's dependencies can be built.
 */
@Context
public class WorkerInfo {

    private final String workerId = IdUtils.create();

    /**
     * The worker unique id.
     *
     * @return The worker id.
     */
    public String getWorkerId() {
        return workerId;
    }
}
