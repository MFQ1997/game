package mai.game.core.task;

import lombok.extern.slf4j.Slf4j;
import mai.game.entity.po.Topic;
import mai.game.service.TopicService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/*
* @Description:定时算出帖子的权重
* */
@Slf4j
public class HotTopicRankingTask extends QuartzJobBean {

    @Autowired
    private TopicService topicService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("LikeTask-------- {}", sdf.format(new Date()));
        List<Topic> hotTopicList = topicService.hotTopicRanking();
    }
}
