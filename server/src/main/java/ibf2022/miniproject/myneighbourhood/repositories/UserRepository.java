package ibf2022.miniproject.myneighbourhood.repositories;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import ibf2022.miniproject.myneighbourhood.model.Post;
import ibf2022.miniproject.myneighbourhood.model.User;


@Repository
public class UserRepository {
    private static final String INSERT_USER_SQL
            = "insert into users (user_name, location, lat, lng, email, is_email_verified, user_password, user_pic_url, join_date) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    public static final String GET_USER_BY_EMAIL_AND_PASSWORD
     = "select user_id, user_name, location, lat, lng, user_pic_url, email from users where email = ? and user_password = ?";

     public static final String GET_USER_BY_USERID
     = "select user_id, user_name, location, lat, lng, user_pic_url, email from users where user_id = ?";

    private static final String INSERT_POST_SQL
            = "insert into posts (user_id, description, title, category, image_url, post_date) values (?, ?, ?, ?, ?, ?)";

    private static final String GET_POSTS_SQL
            = "select * from posts order by post_date, description";
    
    private static final String DELETE_POST_BY_POSTID
            = "delete from posts where post_id = ?";
   
     @Autowired
    private DataSource dataSource;
    
    @Autowired
    private JdbcTemplate template;

    
    public void signUp(String userName, String location, Double lat, Double lng, 
    String email, Boolean isEmailVerified, String userPassword, String userPicUrl, Date joinDate) 
    throws SQLException, IOException{
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(INSERT_USER_SQL)){
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

    public void createPost(int userId, String title, String description, String category, 
    String imageUrl, Date postDate) {
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(INSERT_POST_SQL)){
            prstmt.setInt(1, userId);  
            prstmt.setString(2, description);
            prstmt.setString(3, title); 
            prstmt.setString(4, category);
            prstmt.setString(5, imageUrl);
            prstmt.setDate(6, postDate);
            prstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Post> getPosts() {
    return template.query(
        GET_POSTS_SQL,
        (ResultSet rs) -> {
            List<Post> posts = new ArrayList<>();
            while (rs.next()) {
                Post post = Post.populate(rs); 
                posts.add(post);
            }
            return posts;
        });
    }

    public Optional<User> getLocationByUserId(int userId) {
    return template.query(
        GET_USER_BY_USERID,
        (ResultSet rs) -> {
            if (!rs.next())
                return Optional.empty();
            final User user = User.populateLatNLng(rs);
            
            System.out.println("location>>>>>> " + user.getLocation());
            System.out.println("lat>>>>>> " + user.getLat());
            System.out.println("lng>>>>>> " + user.getLng());
            return Optional.of(user);
        },
        userId);
        
    }
    public Boolean deletePost(int postId) {
        try(Connection con = dataSource.getConnection(); 
            PreparedStatement prstmt = con.prepareStatement(DELETE_POST_BY_POSTID)){
            prstmt.setInt(1, postId);  
            prstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
}
