package paba.project.berita_online.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface NewsDao {
    @Query("SELECT * FROM news")
    suspend fun getAllNews(): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNews(news: paba.project.berita_online.database.NewsEntity)

    @Query("SELECT * FROM news WHERE id = :newsId")
    suspend fun getNewsById(newsId: Int): NewsEntity?

    @Delete
    suspend fun deleteNews(news: NewsEntity)

    @Query("SELECT * FROM news WHERE id IN (:newsIds)")
    suspend fun getNewsByIds(newsIds: List<Int>): List<NewsEntity>
}