package ibf2022.miniproject.myneighbourhood.repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.myneighbourhood.model.User;


@Repository
public class UserRepository {
    private static final String INSERT_POST_SQL
            = "insert into users (user_name, location, lat, lng, email, is_email_verified, user_password, user_pic_url, join_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
      
    // private static final String SQL_GET_POST_BY_POSTID
    //         = "select * from users where userId = ?";
            
    public static final String GET_USER_BY_EMAIL_AND_PASSWORD
     = "select user_id, user_name, location, user_pic_url, email from users where email = ? and user_password = ?";

     public static final String GET_USER_BY_USERID
     = "select user_id, user_name, location, user_pic_url, email from users where user_id = ?";
   
     @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate template;

    
    public void signUp(String userName, String location, Double lat, Double lng, 
    String email, Boolean isEmailVerified, String userPassword, String userPicUrl, Date joinDate) 
    throws SQLException, IOException{
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(INSERT_POST_SQL)){
            prstmt.setString(1, userName);  
            prstmt.setString(2, location);
            prstmt.setDouble(3, lat);
            prstmt.setDouble(4, lng);
            prstmt.setString(5, email);
            prstmt.setBoolean(6, isEmailVerified);
            prstmt.setString(7, userPassword);
            prstmt.setString(8, userPicUrl);
            prstmt.setDate(9, joinDate);
            prstmt.executeUpdate();

            
        }
    }
    
    public Optional<User> getDetailsByEmail(String email, String password) {
    return template.query(
        GET_USER_BY_EMAIL_AND_PASSWORD,
        (ResultSet rs) -> {
            if (!rs.next())
                return Optional.empty();
            final User user = User.populate(rs);
            return Optional.of(user);
        },
        email, password);
    }

    public Optional<User> getDetailsByUserId(int userId) {
    return template.query(
        GET_USER_BY_USERID,
        (ResultSet rs) -> {
            if (!rs.next())
                return Optional.empty();
            final User user = User.populate(rs);
            return Optional.of(user);
        },
        userId);
    }
}
