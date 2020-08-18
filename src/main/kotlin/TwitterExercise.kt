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
//    val tweetsUsersCanSee = mutableListOf<Pair<TweetId, MutableList<UserId>>>()

    override fun postTweet(userId: UserId, tweetId: TweetId) {
//        tweetsUsersCanSee.add(Pair(1, mutableListOf(2)))
        println("TODO postTweet - userId: $userId, tweetId: $tweetId")
    }

    override fun getNewsFeed(userId: UserId): Observable<List<TweetId>> {
        println("TODO getNewsFeed - userId: $userId")
        return Observable.just(emptyList())
    }

    override fun follow(followerId: FollowerId, followeeId: FolloweeId) {
        println("TODO follow - followerId: $followerId, followeeId: $followeeId")
    }

    override fun unfollow(followerId: FollowerId, followeeId: FolloweeId) {
        println("TODO unfollow - followerId: $followerId, followeeId: $followeeId")
    }
}