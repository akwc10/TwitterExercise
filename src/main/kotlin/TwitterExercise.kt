import io.reactivex.Observable

/**
 * Design a simplified version of Twitter where users can post tweets,
 * follow/unfollow another user and is able to see the 10 most recent tweets in the user's news feed.
 *
 * Your design should support the following methods:
 *
 * postTweet(userId, tweetId): Compose a new tweet.
 *
 * get News Feed(user Id): Retrieve the 10 most recent tweet ids in the user's news feed.
 * Each item in the news feed must be posted by users who the user followed or by the user herself.
 * Tweets must be ordered from most recent to least recent.
 *
 * follow(followerId, followed): Follower follows a followee.
 *
 * unfollow(followerId, followeeId): Follower unfollow a followee.
 */

class TwitterExercise : ITwitterExercise {
    private val twitterRepository = TwitterRepository()

    override fun postTweet(userId: UserId, tweetId: TweetId) {
        twitterRepository.postTweet(userId, tweetId)
    }

    override fun getNewsFeed(userId: UserId): Observable<List<TweetId>> = twitterRepository.getNewsFeed(userId)

    override fun follow(followerId: FollowerId, followeeId: FolloweeId) {
        twitterRepository.follow(followerId, followeeId)
    }

    override fun unfollow(followerId: FollowerId, followeeId: FolloweeId) {
        twitterRepository.unfollow(followerId, followeeId)
    }
}