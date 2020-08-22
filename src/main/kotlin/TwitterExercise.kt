import io.reactivex.Observable
import java.time.Instant


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

data class Tweet(val tweetId: TweetId, val userId: UserId, val now: Instant = Instant.now())

class TwitterExercise : ITwitterExercise {
    //TODO - Handle duplicates?
    val tweetIdObservable = Observable.create<List<TweetId>> { emitter ->
        emitter.onNext(emptyList())
    }
    val twitterRepository = TwitterRepository()


    override fun postTweet(userId: UserId, tweetId: TweetId) {
        twitterRepository.postTweet(userId, tweetId)
    }

    override fun getNewsFeed(userId: UserId): Observable<List<TweetId>> {
        twitterRepository.getNewsFeed(userId)
        return Observable.just(emptyList())
    }

    override fun follow(followerId: FollowerId, followeeId: FolloweeId) {
        twitterRepository.follow(followerId, followeeId)
    }

    override fun unfollow(followerId: FollowerId, followeeId: FolloweeId) {
        twitterRepository.unfollow(followerId, followeeId)
    }
}

class TwitterRepository {
    private val tweets = mutableSetOf<Tweet>()
    private val follows = mutableSetOf<Pair<FollowerId, FolloweeId>>()

    fun postTweet(userId: UserId, tweetId: TweetId) {
        if (tweets.find { it.tweetId == tweetId } == null) {
            tweets.add(Tweet(tweetId, userId))
            logPostTweet(userId, tweetId)
        } else {
            println("Duplicate tweetId: $tweetId not posted")
        }
    }

    fun getNewsFeed(userId: UserId): Observable<List<TweetId>> {
        val userIds = listOf(userId) + follows.filter { follow -> follow.second == userId }.map { it.first }
        val tenMostRecentTweets = tweets.filter { userIds.contains(it.userId) }.sortedByDescending { it.now }.take(10)

        logGetNewsFeed(userIds, tenMostRecentTweets)
        return Observable.just(emptyList())
    }

    fun follow(followerId: FollowerId, followeeId: FolloweeId) {
        follows.add(Pair(followerId, followeeId))
        logFollowUnfollow("FOLLOW", followerId, followeeId)
    }

    fun unfollow(followerId: FollowerId, followeeId: FolloweeId) {
        follows.remove(Pair(followerId, followeeId))
        logFollowUnfollow("UNFOLLOW", followerId, followeeId)
    }

    private fun logPostTweet(userId: UserId, tweetId: TweetId) {
        print("POSTTWEET - userId: $userId, tweetId: $tweetId - [")
        tweets.forEachIndexed { index, tweet ->
            print("(${tweet.userId}, ${tweet.tweetId})${notEnd(index, tweets.size)}${end(index, tweets.size)}")
        }
    }

    private fun logGetNewsFeed(userIds: List<UserId>, tenMostRecentTweets: List<Tweet>) {
        println("getNewsFeed userIds: $userIds")
        tenMostRecentTweets.forEach { println("tweetId: ${it.tweetId}, userIds: ${it.userId}, timeStamp: ${it.now}") }
    }

    private fun logFollowUnfollow(followUnfollow: String, followerId: Int, followeeId: Int) {
        print("$followUnfollow - followerId: $followerId, followeeId: $followeeId - [")
        follows.forEachIndexed { index, pair ->
            print("(${pair.first}, ${pair.second})${notEnd(index, follows.size)}${end(index, follows.size)}")
        }
    }

    private fun notEnd(index: Int, size: Int, string: String = ", ") = if (index < size - 1) string else ""

    private fun end(index: Int, size: Int, string: String = "]\n") = if (index >= size - 1) string else ""
}
