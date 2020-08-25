import io.reactivex.Observable
import io.reactivex.subjects.ReplaySubject
import java.time.Instant

class TwitterRepository {
    //    TODO - Implement immutable
//    TODO - How to emit whenever MutableSet<TweetId> changes?
    private val MAX_TWEETS = 10
    private val tweets = mutableSetOf<Tweet>()
    private val follows = mutableSetOf<Follow>()
    private val tweetsSubject = ReplaySubject.create<Set<Tweet>>()
    private val followsSubject = ReplaySubject.create<Set<Follow>>()

    fun postTweet(userId: UserId, tweetId: TweetId) {
        if (tweets.find { it.tweetId == tweetId } == null) {
            tweets.add(Tweet(tweetId, userId))
            tweetsSubject.onNext(tweets)
            logPostTweet(userId, tweetId)
        } else {
            println("Duplicate tweetId: $tweetId not posted")
        }
    }

    fun getNewsFeed(userId: UserId): Observable<List<TweetId>> =
        Observable.combineLatest(followsSubject, tweetsSubject, { follows: Set<Follow>, tweets: Set<Tweet> ->
            val userIdsOfFollowerAndFollowees =
                listOf(userId) + follows.filter { follow -> follow.followerId == userId }
                    .map { follow -> follow.followeeId }
            println("userIdsOfFollowerAndFollowees: $userIdsOfFollowerAndFollowees")
//            println("tenMostRecentTweetIds: $tenMostRecentTweetIds")
            tweets.filter { tweet -> userIdsOfFollowerAndFollowees.contains(tweet.userId) }
                .sortedByDescending { tweet -> tweet.now }
                .take(MAX_TWEETS)
                .map { tweet -> tweet.tweetId }
        })

//        followsSubject.subscribe { follows ->
//            val userIdsOfFollowerAndFollowees =
//                listOf(userId) + follows.filter { follow -> follow.followerId == userId }
//                    .map { follow -> follow.followeeId }
//            println("userIdsOfFollowerAndFollowees: $userIdsOfFollowerAndFollowees")
//            tweetsSubject.subscribe { tweets ->
//                val tenMostRecentTweetIds =
//                    tweets.filter { tweet -> userIdsOfFollowerAndFollowees.contains(tweet.userId) }
//                        .sortedByDescending { tweet -> tweet.now }
//                        .take(MAX_TWEETS)
//                        .map { tweet -> tweet.tweetId }
//                println("tenMostRecentTweetIds: $tenMostRecentTweetIds")
//                it.onNext(tenMostRecentTweetIds)
//            }
//        }

    fun follow(followerId: FollowerId, followeeId: FolloweeId) {
        follows.add(Follow(followerId, followeeId))
        followsSubject.onNext(follows)
        logFollowUnfollow(followerId, followeeId)
    }

    fun unfollow(followerId: FollowerId, followeeId: FolloweeId) {
        follows.remove(Follow(followerId, followeeId))
        followsSubject.onNext(follows)
        logFollowUnfollow(followerId, followeeId, "UNFOLLOW")
    }

    private fun logPostTweet(userId: UserId, tweetId: TweetId) {
        print("POSTTWEET - userId: $userId, tweetId: $tweetId - [")
        tweets.forEachIndexed { index, tweet ->
            print("(${tweet.userId}, ${tweet.tweetId})${appendNotEnd(index, tweets.size)}")
        }
        println("]")
    }

    private fun logGetNewsFeed(userIds: List<UserId>, tenMostRecentTweets: List<Tweet>) {
        println("getNewsFeed userIds: $userIds")
        tenMostRecentTweets.forEach { println("tweetId: ${it.tweetId}, userIds: ${it.userId}, timeStamp: ${it.now}") }
    }

    private fun logFollowUnfollow(followerId: Int, followeeId: Int, followUnfollow: String = "FOLLOW") {
        print("$followUnfollow - followerId: $followerId, followeeId: $followeeId - [")
        follows.forEachIndexed { index, follow ->
            print("(${follow.followerId}, ${follow.followeeId})${appendNotEnd(index, follows.size)}")
        }
        println("]")
    }
}

data class Tweet(val tweetId: TweetId, val userId: UserId, val now: Instant = Instant.now())

data class Follow(val followerId: FollowerId, val followeeId: FolloweeId)
