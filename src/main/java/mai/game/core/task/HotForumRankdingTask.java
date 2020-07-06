package mai.game.core.task;

import lombok.extern.slf4j.Slf4j;
import mai.game.entity.po.ForumModule;
import mai.game.entity.po.Topic;
import mai.game.service.ForumModuleService;
import mai.game.service.ForumModuleWeightService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Slf4j
public class HotForumRankdingTask extends QuartzJobBean {

    private ForumModuleService forumModuleService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        log.info("LikeTask-------- {}", sdf.format(new Date()));
        List<ForumModule> hotTopicList = forumModuleService.hotForumModuleRanking();
    }
}
