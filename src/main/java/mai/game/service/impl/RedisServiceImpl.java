package mai.game.service.impl;

import lombok.extern.slf4j.Slf4j;
import mai.game.core.enums.CollectedStatusEnum;
import mai.game.core.enums.FollowedStatusEnum;
import mai.game.core.enums.JoinedStatusEnum;
import mai.game.core.enums.LikedStatusEnum;
import mai.game.core.util.RedisKeyUtil;
import mai.game.dto.CollectedCountDTO;
import mai.game.dto.FollowedCountDTO;
import mai.game.dto.JoinedCountDTO;
import mai.game.dto.LikedCountDTO;
import mai.game.entity.user.UserCollect;
import mai.game.entity.user.UserFollow;
import mai.game.entity.user.UserForumModule;
import mai.game.entity.user.UserLike;
import mai.game.service.RedisService;
import mai.game.service.UserLikeService;
import org.apache.commons.collections4.Get;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    UserLikeService likedService;

    /**
     * @Description:点赞。状态为1,将用户点赞的数据暂时先存储在redis中先，而后按照定时任务再将数据从Redis中持久化到MySQL数据库中
     *
     * @param likedTopicId
     * @param likedUserId
     */
    @Override
    public void saveLiked2Redis(String likedTopicId, String likedUserId) {
        String key = RedisKeyUtil.getLikedKey(likedTopicId, likedUserId);
        Integer b = hasUserFollow(Integer.parseInt(likedTopicId), Integer.parseInt(likedUserId));
        if (b.equals(-1)){
            deleteFollowedFromRedis(likedTopicId,likedUserId);
        }else {
            //将被点赞的帖子的id和点赞这篇帖子的用户的id拼成 xx::yy 的形式作为redis的key，将点赞状态作为value存储到Redis中
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.LIKE.getCode());
        }
    }

    /**
     * @Description:取消点赞。将状态改变为0,这个操作和上面的收藏操作基本一致，只是将value设置为0，表示取消点赞
     *
     * @param likedTopicId
     * @param likedUserId
     */
    @Override
    public void unlikeFromRedis(String likedTopicId, String likedUserId) {
        String key = RedisKeyUtil.getLikedKey(likedTopicId, likedUserId);
        Integer b = hasUserZan(Integer.parseInt(likedTopicId), Integer.parseInt(likedUserId));
        if (b.equals(1)){
            //当取消点赞的时候，Redis还保存着之前的点赞成功的记录的时候则进行删除
            deleteLikedFromRedis(likedTopicId,likedUserId);
        }else {
            //当取消点赞的时候，Redis已经把之前的点赞成功的记录同步到MySQL数据库去的时候，则将状态为取消点赞的记录插入到Redis，等待后面同步的操作
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_LIKED, key, LikedStatusEnum.UNLIKE.getCode());
        }

    }

    /**
     * @Description:从Redis中删除一条点赞数据
     *
     * @param likedTopicId
     * @param likedUserId
     */
    @Override
    public void deleteLikedFromRedis(String likedTopicId, String likedUserId) {
        String key = RedisKeyUtil.getLikedKey(likedTopicId, likedUserId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
    }

    /**
     * @Description:该帖子的点赞数加1
     *
     * @param likedTopicId
     */
    @Override
    public void incrementLikedCount(String likedTopicId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedTopicId, 1);
    }

    /**
     * @Description:该帖子的点赞数减1
     *
     * @param likedTopicId
     */
    @Override
    public void decrementLikedCount(String likedTopicId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, likedTopicId, -1);
    }

    /*
    * @Description:从redis中获取用户点赞帖子的数据
    * */
    @Override
    public List<UserLike> getLikedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        List<UserLike> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            //分离出 likedUserId，likedPostId
            String[] split = key.split("::");
            String likedTopicId = split[0];     //截取被点赞的帖子的id
            String likedUserId = split[1];      //截取点赞这篇帖子的用户id
            Integer value = (Integer) entry.getValue();

            //组装成 UserLike 对象
            //UserLike userLike = new UserLike(likedUserId, likedPostId, value);
            UserLike userLike = new UserLike();
            userLike.setLikedUserId(likedUserId);
            userLike.setLikedTopicId(likedTopicId);
            userLike.setStatus(value);
            userLike.setCreateTime(new Date());
            userLike.setUpdateTime(new Date());
            list.add(userLike);
            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED, key);
        }

        return list;
    }

    /*
    * @Description:从Redis中获取帖子的点赞量，就是这篇帖子被点赞了多少次
    * */
    @Override
    public List<LikedCountDTO> getLikedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, ScanOptions.NONE);
        List<LikedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            LikedCountDTO dto = new LikedCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_LIKED_COUNT, key);
        }
        return list;
    }

    @Override
    public Integer hasUserZan(Integer topicId, Integer userId) {
        //处理要判断的信息： topicId::userId
        String msgKey = RedisKeyUtil.getLikedKey(String.valueOf(topicId), String.valueOf(userId));
        //获取redis的数据
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_LIKED, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Integer status = Integer.parseInt(entry.getValue().toString());
            System.out.println("Redis中存储的状态是："+status);
            System.out.println("对比的数据："+msgKey);
            System.out.println("从redis 中获取出来的数据："+key);
            if (key.equals(msgKey) && status.equals(1))
                return 1;
            if (key.equals(msgKey) && status.equals(0))
                return -1;
        }
        return 0;
    }

    @Override
    public Integer hasUserCollect(Integer topicId, Integer userId) {

        //处理要判断的信息： topicId::userId
        String msgKey = RedisKeyUtil.getLikedKey(String.valueOf(topicId), String.valueOf(userId));
        //获取redis的数据
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_COLLECTED, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Integer status = (Integer) entry.getValue();
            if (key.equals(msgKey) && status.equals(1))
                return 1;
            if(key.equals(msgKey) && status.equals(0))//当存在为0的数据的时候，代表redis存在用户取消点赞的记录，，但是此时还没有同步到MySQL
                return -1;
        }
        return 0;
    }




    /*
    * __________________________________________________________入住板块
    * */
    @Override
    public void saveJoin2Redis(String joinedForumModuleId,String joinedUserId) {
        String key = RedisKeyUtil.getJoinedKey(joinedForumModuleId, joinedUserId);
        Integer b = hasUserFollow(Integer.parseInt(joinedForumModuleId), Integer.parseInt(joinedUserId));
        if (b.equals(-1)){
            deleteFollowedFromRedis(joinedForumModuleId,joinedUserId);
        }else {
            //将被点赞的帖子的id和点赞这篇帖子的用户的id拼成 xx::yy 的形式作为redis的key，将点赞状态作为value存储到Redis中
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_JOINED, key, JoinedStatusEnum.JOIN.getCode());
        }
    }
    @Override
    public void unJoinFromRedis(String joinedForumModuleId,String joinedUserId) {
        String key = RedisKeyUtil.getJoinedKey(joinedForumModuleId, joinedUserId);
        Integer b = hasUserJoin(Integer.parseInt(joinedForumModuleId), Integer.parseInt(joinedUserId));
        if (b.equals(1)){
            deleteJoinedFromRedis(joinedForumModuleId,joinedUserId);
        }else {
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_JOINED, key, JoinedStatusEnum.UNJOIN.getCode());
        }
    }
    @Override
    public void deleteJoinedFromRedis(String joinedForumModuleId,String joinedUserId) {
        String key = RedisKeyUtil.getJoinedKey(joinedForumModuleId, joinedUserId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_JOINED, key);
    }
    @Override
    public void incrementJoinedCount(String joinedForumModuleId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_JOINED_COUNT, joinedForumModuleId, 1);
    }
    @Override
    public void decrementJoinedCount(String joinedForumModuleId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_JOINED_COUNT, joinedForumModuleId, -1);
    }
    @Override
    public List<UserForumModule> getJoinedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_JOINED, ScanOptions.NONE);
        List<UserForumModule> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String joinedForumModuleId = split[0];
            String joinedUserId = split[1];
            Integer value = (Integer) entry.getValue();

            UserForumModule userForumModule = new UserForumModule();
            userForumModule.setForumModuleId(Integer.parseInt(joinedForumModuleId));
            userForumModule.setUserId(Integer.parseInt(joinedUserId));
            userForumModule.setStatus(value);
            userForumModule.setTime(new Date());
            list.add(userForumModule);
            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_JOINED, key);
        }
        return list;
    }
    @Override
    public List<JoinedCountDTO> getJoinedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_JOINED_COUNT, ScanOptions.NONE);
        List<JoinedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            JoinedCountDTO dto = new JoinedCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_JOINED_COUNT, key);
        }
        return list;
    }
    @Override
    public Integer hasUserJoin(int forumId, Integer userId) {
        //处理要判断的信息： topicId::userId
        String msgKey = RedisKeyUtil.getJoinedKey(String.valueOf(forumId), String.valueOf(userId));
        //获取redis的数据
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_JOINED, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Integer status = Integer.parseInt(entry.getValue().toString());
            if (key.equals(msgKey) && status.equals(1))
                return 1;
            if(key.equals(msgKey) && status.equals(0))//当存在为0的数据的时候，代表redis存在用户取消点赞的记录，，但是此时还没有同步到MySQL
                return -1;
        }
        return 0;
    }

    @Override
    public List<UserForumModule> getUserJoinedDataFromRedis(Integer userId) {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_JOINED, ScanOptions.NONE);
        List<UserForumModule> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String joinedForumModuleId = split[0];
            String joinedUserId = split[1];
            if (userId.equals(Integer.parseInt(joinedUserId))) {
                UserForumModule userForumModule = new UserForumModule();
                userForumModule.setForumModuleId(Integer.parseInt(joinedForumModuleId));
                list.add(userForumModule);
            }
        }
        return list;
    }

    @Override
    public String getLikedCountFromRedisByTopicId(Integer id) {
        return (String)redisTemplate.opsForValue().get(id);
    }

    /*
    * __________________________________________________________________________关注
    * */
    @Override
    public void saveFollow2Redis(String followedUserId, String followUserId) {
        String key = RedisKeyUtil.getFollowedKey(followedUserId, followUserId);
        Integer b = hasUserFollow(Integer.parseInt(followedUserId), Integer.parseInt(followUserId));
        if (b.equals(-1)){
            deleteFollowedFromRedis(followedUserId,followUserId);
        }else {
            //将被点赞的帖子的id和点赞这篇帖子的用户的id拼成 xx::yy 的形式作为redis的key，将点赞状态作为value存储到Redis中
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, key, FollowedStatusEnum.FOLLOW.getCode());
        }
    }
    @Override
    public void unFollowFromRedis(String followedUserId, String followUserId) {
        String key = RedisKeyUtil.getFollowedKey(followedUserId, followUserId);
        Integer b = hasUserFollow(Integer.parseInt(followedUserId), Integer.parseInt(followUserId));
        if (b.equals(1)){
            //当取消点赞的时候，Redis还保存着之前的点赞成功的记录的时候则进行删除
            deleteFollowedFromRedis(followedUserId,followUserId);
        }else {
            //当取消点赞的时候，Redis已经把之前的点赞成功的记录同步到MySQL数据库去的时候，则将状态为取消点赞的记录插入到Redis，等待后面同步的操作
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, key, FollowedStatusEnum.UNFOLLOW.getCode());
        }
    }
    @Override
    public void deleteFollowedFromRedis(String followedUserId, String followUserId) {
        String key = RedisKeyUtil.getLikedKey(followedUserId, followUserId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, key);
    }
    @Override
    public void incrementFollowedCount(String followedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_FOLLOWED_COUNT, followedUserId, 1);
    }
    @Override
    public void decrementFollowedCount(String followedUserId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_FOLLOWED_COUNT, followedUserId, -1);
    }
    @Override
    public List<UserFollow> getFollowedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, ScanOptions.NONE);

        List<UserFollow> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String followedUserId = split[0];
            String followUserId = split[1];
            Integer value = (Integer) entry.getValue();

            UserFollow userFollow = new UserFollow();
            userFollow.setFollowedUserId(followedUserId);
            userFollow.setUserId(followUserId);
            userFollow.setStatus(value);
            list.add(userFollow);
            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, key);
        }

        return list;
    }
    @Override
    public List<FollowedCountDTO> getFollowedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_FOLLOWED_COUNT, ScanOptions.NONE);
        List<FollowedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            FollowedCountDTO dto = new FollowedCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_FOLLOWED_COUNT, key);
        }
        return list;
    }
    @Override
    public Integer hasUserFollow(Integer masterId, Integer userId) {
        //处理要判断的信息： topicId::userId
        String msgKey = RedisKeyUtil.getFollowedKey(String.valueOf(masterId), String.valueOf(userId));
        //获取redis的数据
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_FOLLOWED, ScanOptions.NONE);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            Integer status = Integer.parseInt(entry.getValue().toString());
            if (key.equals(msgKey) && status.equals(1))
                return 1;
            if(key.equals(msgKey) && status.equals(0))//当存在为0的数据的时候，代表redis存在用户取消点赞的记录，，但是此时还没有同步到MySQL
                return -1;
        }
        return 0;
    }


    /*
     * ————————————————————————————————————————————————————————————————————————收藏
     * */
    @Override
    public void saveCollect2Redis(String CollectedTopicId, String CollectedUserId) {
        String key = RedisKeyUtil.getCollectedKey(CollectedTopicId, CollectedUserId);
        Integer b = hasUserFollow(Integer.parseInt(CollectedTopicId), Integer.parseInt(CollectedUserId));
        if (b.equals(-1)){
            deleteFollowedFromRedis(CollectedTopicId,CollectedUserId);
        }else {
            //将被点赞的帖子的id和点赞这篇帖子的用户的id拼成 xx::yy 的形式作为redis的key，将点赞状态作为value存储到Redis中
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_COLLECTED, key, CollectedStatusEnum.COLLECT.getCode());
        }
    }
    @Override
    public void unCollectFromRedis(String CollectedTopicId, String CollectedUserId) {
        String key = RedisKeyUtil.getCollectedKey(CollectedTopicId, CollectedUserId);

        Integer b = hasUserCollect(Integer.parseInt(CollectedTopicId), Integer.parseInt(CollectedUserId));
        if (b.equals(1)){
            //当取消收藏的时候，Redis还保存着之前的收藏成功的记录的时候则进行删除
            deleteCollectedFromRedis(CollectedTopicId,CollectedUserId);
        }else {
            //当取消收藏的时候，Redis已经把之前的收藏成功的记录同步到MySQL数据库去的时候，则将状态为取消收藏的记录插入到Redis，等待后面同步的操作
            redisTemplate.opsForHash().put(RedisKeyUtil.MAP_KEY_USER_COLLECTED, key, CollectedStatusEnum.UNCOLLECT.getCode());
        }

    }

    @Override
    public void deleteCollectedFromRedis(String CollectedTopicId, String CollectedUserId) {
        String key = RedisKeyUtil.getCollectedKey(CollectedTopicId, CollectedUserId);
        redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_COLLECTED, key);
    }

    @Override
    public void incrementCollectedCount(String CollectedTopicId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_COLLECTED_COUNT, CollectedTopicId, 1);
    }

    @Override
    public void decrementCollectedCount(String CollectedTopicId) {
        redisTemplate.opsForHash().increment(RedisKeyUtil.MAP_KEY_USER_COLLECTED_COUNT, CollectedTopicId, -1);
    }

    @Override
    public List<UserCollect> getCollectedDataFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_COLLECTED, ScanOptions.NONE);
        List<UserCollect> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            String key = (String) entry.getKey();
            String[] split = key.split("::");
            String CollectedTopicId = split[0];
            String CollectedUserId = split[1];
            Integer value = (Integer) entry.getValue();

            UserCollect userCollect = new UserCollect();
            userCollect.settTopic(Integer.parseInt(CollectedTopicId));
            userCollect.settUser(Integer.parseInt(CollectedUserId));
            userCollect.setTime(new Date());
            userCollect.setStatus(value);
            list.add(userCollect);
            //存到 list 后从 Redis 中删除
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_COLLECTED, key);
        }

        return list;
    }

    @Override
    public List<CollectedCountDTO> getCollectedCountFromRedis() {
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(RedisKeyUtil.MAP_KEY_USER_COLLECTED_COUNT, ScanOptions.NONE);
        List<CollectedCountDTO> list = new ArrayList<>();
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> map = cursor.next();
            //将点赞数量存储在 LikedCountDT
            String key = (String) map.getKey();
            CollectedCountDTO dto = new CollectedCountDTO(key, (Integer) map.getValue());
            list.add(dto);
            //从Redis中删除这条记录
            redisTemplate.opsForHash().delete(RedisKeyUtil.MAP_KEY_USER_COLLECTED_COUNT, key);
        }
        return list;
    }
}