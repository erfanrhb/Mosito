package rahbari.erfan.mosito.resources.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import rahbari.erfan.mosito.models.User;

@Dao
public interface UserDao {
    @Insert
    void insert(User user);

    @Update
    void update(User user);

    @Query("SELECT * FROM users LIMIT 1")
    LiveData<User> live();

    @Query("SELECT * FROM users LIMIT 1")
    User user();

    @Query("SELECT * FROM users")
    List<User> users();

    @Query("DELETE FROM users WHERE id = :id")
    void delete(long id);

    @Query("SELECT COUNT(*) FROM users WHERE id = :Id")
    int count(long Id);
}
