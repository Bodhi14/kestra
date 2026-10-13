package io.kestra.core.runners;

import io.kestra.core.repositories.CheckpointRepositoryInterface;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.util.List;
import java.util.Optional;

@Singleton
public class RunContextCheckpointFactory {
    // TODO use a service and make a gRPC bridge for it
    @Inject
    CheckpointRepositoryInterface checkpointRepository;

    public Checkpoint create(RunContext runContext) {
        return new CheckpointImpl(runContext, checkpointRepository);
    }

    static class CheckpointImpl implements Checkpoint {
        private final String tenantId;
        private final RunContext.TaskRunInfo taskRunInfo;
        private final CheckpointRepositoryInterface checkpointRepository;

        CheckpointImpl(RunContext runContext, CheckpointRepositoryInterface checkpointRepository) {
            this.tenantId = runContext.flowInfo().tenantId();
            this.taskRunInfo = runContext.taskRunInfo();
            this.checkpointRepository = checkpointRepository;
        }

        @Override
        public List<io.kestra.core.checkpoint.Checkpoint> list() {
            return checkpointRepository.findByTaskRun(tenantId, taskRunInfo.executionId(), taskRunInfo.taskRunId());
        }

        @Override
        public Optional<String> get(String key) {
            return checkpointRepository.findById(tenantId, taskRunInfo.executionId(), taskRunInfo.taskRunId(), key).map(it -> it.value());
        }

        @Override
        public void set(String key, String value) {
            var checkpoint = new io.kestra.core.checkpoint.Checkpoint(
                tenantId,
                taskRunInfo.executionId(),
                taskRunInfo.taskRunId(),
                key,
                value
            );

            checkpointRepository.save(checkpoint);
        }

    }
}
