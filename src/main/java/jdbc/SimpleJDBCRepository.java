package jdbc;


import jdbc.exceptions.SimpleJDBCRepositoryException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SimpleJDBCRepository {

    private static final String CREATE_USER = "INSERT INTO myusers (firstname, lastname, age) " +
        "VALUES (?, ?, ?)";
    private static final String UPDATE_USER = "UPDATE myusers SET firstname = ?, lastname = ?, " +
        "age = ? WHERE id = ?";
    private static final String DELETE_USER = "DELETE FROM myusers WHERE id = ?";
    private static final String FIND_USER_BY_ID = "SELECT * FROM myusers WHERE id = ?";
    private static final String FIND_USER_BY_NAME = "SELECT * FROM myusers WHERE firstname = ?";
    private static final String FIND_ALL_USERS = "SELECT * FROM myusers";
    private static final String FIRST_NAME_COLUMN = "firstname";
    private static final String LAST_NAME_COLUMN = "lastname";
    private static final String AGE_COLUMN = "age";

    public Long createUser(User user) {
        Long id = null;
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(CREATE_USER, Statement.RETURN_GENERATED_KEYS)
        ) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    id = rs.getLong(1);
                }
                rs.close();
            }
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
        return id;
    }

    public User findUserById(Long userId) {
        User user = null;
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_ID)
        ) {
            ps.setLong(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getLong("id"),
                    rs.getString(FIRST_NAME_COLUMN),
                    rs.getString(LAST_NAME_COLUMN),
                    rs.getInt(AGE_COLUMN)
                );
            }
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
        return user;
    }

    public User findUserByName(String userName) {
        User user = null;
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_USER_BY_NAME)
        ) {
            ps.setString(1, userName);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                user = new User(
                    rs.getLong("id"),
                    rs.getString(FIRST_NAME_COLUMN),
                    rs.getString(LAST_NAME_COLUMN),
                    rs.getInt(AGE_COLUMN)
                );
            }
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
        return user;
    }

    public List<User> findAllUser() {
        List<User> users = new ArrayList<>();
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(FIND_ALL_USERS);
            ResultSet rs = ps.executeQuery()
        ) {
            while (rs.next()) {
                User user = new User(
                    rs.getLong("id"),
                    rs.getString(FIRST_NAME_COLUMN),
                    rs.getString(LAST_NAME_COLUMN),
                    rs.getInt(AGE_COLUMN)
                );
                users.add(user);
            }
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
        return users;
    }

    public User updateUser(User user) {
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(UPDATE_USER)
        ) {
            ps.setString(1, user.getFirstName());
            ps.setString(2, user.getLastName());
            ps.setInt(3, user.getAge());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
        return findUserById(user.getId());
    }

    public void deleteUser(Long userId) {
        try (
            Connection connection = CustomDataSource.getInstance().getConnection();
            PreparedStatement ps = connection.prepareStatement(DELETE_USER)
        ) {
            ps.setLong(1, userId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new SimpleJDBCRepositoryException(e);
        }
    }
}