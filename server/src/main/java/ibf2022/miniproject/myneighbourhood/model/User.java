package ibf2022.miniproject.myneighbourhood.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class User implements Serializable {
    private Integer userId;
    private String userName;
    private String location;
    private double lat;
    private double lng;
    private String email;
    private boolean isEmailVerified;
    private String password;
    private String userPicUrl;
    private Date joinDate;
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getLocation() {
        return location;
    }
    public void setLocation(String location) {
        this.location = location;
    }
    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public double getLng() {
        return lng;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public boolean isEmailVerified() {
        return isEmailVerified;
    }
    public void setEmailVerified(boolean isEmailVerified) {
        this.isEmailVerified = isEmailVerified;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getUserPicUrl() {
        return userPicUrl;
    }
    public void setUserPicUrl(String userPicUrl) {
        this.userPicUrl = userPicUrl;
    }
    public Date getJoinDate() {
        return joinDate;
    }
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public static User populate(ResultSet rs) throws SQLException{
        final User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setUserName(rs.getString("user_name"));
        user.setLocation(rs.getString("location"));
        user.setUserPicUrl(rs.getString("user_pic_url"));
        user.setEmail(rs.getString("email"));
        user.setLat(rs.getDouble("lat"));
        user.setLng(rs.getDouble("lng"));
        
        
        return user;
    }

    public static User populateLatNLng(ResultSet rs) throws SQLException{
        final User user = new User(); 
        user.setLat(rs.getDouble("lat"));
        user.setLng(rs.getDouble("lng"));  
        user.setLocation(rs.getString("location"));
                  
        return user;
    }
    

  

    
}
