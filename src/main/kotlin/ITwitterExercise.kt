import io.reactivex.Observable

typealias UserId = Int
typealias TweetId = Int
typealias FollowerId = Int
typealias FolloweeId = Int

interface ITwitterExercise {
    fun postTweet(userId: UserId, tweetId: TweetId)
    fun getNewsFeed(userId: UserId): Observable<List<TweetId>>
    fun follow(followerId: FollowerId, followeeId: FolloweeId)
    fun unfollow(followerId: FollowerId, followeeId: FolloweeId)
}