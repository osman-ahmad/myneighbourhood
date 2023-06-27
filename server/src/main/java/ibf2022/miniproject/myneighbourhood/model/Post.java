package ibf2022.miniproject.myneighbourhood.model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Post implements Serializable{
    private String description;
    private String title;
    private String imageUrl;
    private String category;
    private Integer postId;
    private Integer userId;
    private Date postDate;

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Integer getPostId() {
        return postId;
    }
    public void setPostId(Integer postId) {
        this.postId = postId;
    }
    public Integer getUserId() {
        return userId;
    }
    public void setUserId(Integer userId) {
        this.userId = userId;
    }
    public Date getPostDate() {
        return postDate;
    }
    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public static Post populate(ResultSet rs) throws SQLException{
        Post post = new Post();
        post.setDescription(rs.getString("description"));
        post.setTitle(rs.getString("title"));
        post.setImageUrl(rs.getString("image_Url"));
        post.setCategory(rs.getString("category"));
        post.setPostId(rs.getInt("post_Id"));
        post.setUserId(rs.getInt("user_Id"));
        post.setPostDate(rs.getDate("post_Date"));
        return post;
    }
    


}
