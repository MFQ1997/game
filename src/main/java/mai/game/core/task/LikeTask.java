package mai.game.core.task;

import lombok.extern.slf4j.Slf4j;
import mai.game.entity.user.UserFollow;
import mai.game.service.UserCollectService;
import mai.game.service.UserFollowService;
import mai.game.service.UserForumModuleService;
import mai.game.service.UserLikeService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 点赞的定时任务
 */
@Slf4j
public class LikeTask extends QuartzJobBean {

    @Autowired
    UserLikeService likeService;
    @Autowired
    UserCollectService collectService;
    @Autowired
    UserFollowService followService;
    @Autowired
    UserForumModuleService joinService;

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        log.info("LikeTask-------- {}", sdf.format(new Date()));
        //将 Redis 里的点赞信息同步到数据库里
        likeService.transLikedFromRedis2DB();
        likeService.transLikedCountFromRedis2DB();

        log.info("CollectTask---------{}",sdf.format(new Date()));
        //将redis 中的收藏信息同步到数据库中
        collectService.transCollectedFromRedis2DB();
        collectService.transCollectedCountFromRedis2DB();

        log.info("FollowTask---------{}",sdf.format(new Date()));
        followService.transFollowedFromRedis2DB();
        //followService.transFollowedCountFromRedis2DB();

        log.info("JoinTask---------{}",sdf.format(new Date()));
        joinService.transJoinedFromRedis2DB();
        //joinService.transJoinedCountFromRedis2DB();
    }
}
