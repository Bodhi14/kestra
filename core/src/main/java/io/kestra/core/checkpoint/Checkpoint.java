package io.kestra.core.checkpoint;

import io.kestra.core.models.HasUID;
import io.kestra.core.utils.IdUtils;

public record Checkpoint(String tenantId, String executionId, String taskRunId, String key, String value) implements HasUID {
    @Override
    public String uid() {
        return IdUtils.fromParts(tenantId, executionId, taskRunId, key);
    }
}
