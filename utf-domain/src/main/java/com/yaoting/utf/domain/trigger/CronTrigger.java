package com.yaoting.utf.domain.trigger;

import com.yaoting.utf.common.utils.ValidateUtils;
import com.yaoting.utf.domain.job.Job;
import com.yaoting.utf.domain.job.Jobs;
import com.yaoting.utf.domain.job.State;
import com.yaoting.utf.domain.coordinator.local.DefaultLocalCoordinator;
import com.yaoting.utf.domain.tools.job.DistributedJob;
import com.yaoting.utf.domain.tools.lock.LockInfoVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@Slf4j
public class CronTrigger extends DistributedJob implements Trigger {

    @Resource
    private DefaultLocalCoordinator localCoordinator;

    private Long DEFAULT_LOCK_TIME = 10 * 60_000L;

    @Scheduled(cron="0/5 * * * * ?")
    public void doWork() {

        if (!localCoordinator.isWorking()) {
            log.info("Local coordinator hasn't been working, try next time");
            return;
        }

        List<Job> jobs = Jobs.listJobsByStates(State.Ready, State.Frozen);
        if (ValidateUtils.isEmpty(jobs)) {
            log.info("No job need to run, check next time");
            return;
        }

        jobs.forEach(job -> {
            Optional<LockInfoVO> lockOptional = tryLock(this.name() + ":" + job.getId() + "--" + job.getName(), DEFAULT_LOCK_TIME);
            if (lockOptional.isPresent()) {
                log.info("Get lock successful，key: {}，start to run job, JobId: {}", lockOptional.get(), job.getId());
                localCoordinator.addJob(job);

                // after addJob, release lock
                releaseLock(lockOptional.get());
            }
        });
    }

}
