package com.wizvera.templet.config.scheduler;

import com.wizvera.templet.repository.ScheduleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
public class Scheduler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private ScheduleRepository scheduleRepository;

    /**
     * 오른쪽부터 : *년 *월 *일 *시 *분 *초
     */
    @Scheduled(cron = "1 1 14 * * *")
    public void expiredUser() {

        String value = "update user set status = 1 where (date_format(now(), '%Y%m%d') - date_format(modified_date, '%Y%m%d')) > 10000 and status = 0";

        logger.info("userExpiring query >>>>> " + value);

        scheduleRepository.userExpiring();

    }

}
