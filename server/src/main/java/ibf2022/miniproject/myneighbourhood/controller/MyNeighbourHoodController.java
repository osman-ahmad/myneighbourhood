package ibf2022.miniproject.myneighbourhood.controller;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.myneighbourhood.model.Post;
import ibf2022.miniproject.myneighbourhood.model.User;
import ibf2022.miniproject.myneighbourhood.services.GoogleMapService;
import ibf2022.miniproject.myneighbourhood.services.S3Service;
import ibf2022.miniproject.myneighbourhood.services.SqlDatabaseService;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

@Controller
public class MyNeighbourHoodController {
    @Autowired
    private S3Service s3Svc;

    @Autowired
    private SqlDatabaseService sqlDatabaseService;

    @Autowired
    private GoogleMapService googleMapService;

    @PostMapping(path = "/signin", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, 
                                    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> upload(
        @RequestPart MultipartFile profilePic,
        @RequestPart String username,
        @RequestPart String location,
        @RequestPart String email,
        @RequestPart String password
        ) throws SQLException {
        try {
            String imageUrl = s3Svc.upload(profilePic);
            double[] coordinates = googleMapService.getLatLng(location);
            double lat = coordinates[0];
            double lng = coordinates[1];
            
            Boolean isEmailVerified = false;
            Date joinDate   = new Date(System.currentTimeMillis());
            
            this.sqlDatabaseService.upload(username, location, lat, lng, email, isEmailVerified, password, imageUrl, joinDate);
            System.out.println("imageUrl:>>>>>>>>> " + imageUrl);
            System.out.println("username:>>>>>>>>> " + username);
            System.out.println("location:>>>>>>>>> " + location);
            System.out.println("email:>>>>>>>>> " + email);
            System.out.println("password:>>>>>>>>> " + password);
            System.out.println("lat:>>>>>>>>> " + lat);
            System.out.println("lng:>>>>>>>>> " + lng);
            System.out.println("isEmailVerified:>>>>>>>>> " + isEmailVerified);
            System.out.println("joinDate:>>>>>>>>> " + joinDate);
                       
            JsonObject payload = Json.createObjectBuilder()
            .add("imageKey", imageUrl)
            .add("username", username)
            .add("location", location)
            .add("email", email)
            .add("password", password)
            .add("lat", lat)
            .add("lng", lng)
            .add("isEmailVerified", isEmailVerified)
            .add("joinDate", joinDate.toString())
            .build();
            return ResponseEntity.ok(payload.toString());

        } catch (IOException e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
        
    }    

    @GetMapping(path="/get-details/{emailParam}/{password}")
    public ResponseEntity<String> retrieveDetails(@PathVariable String emailParam, @PathVariable String password){
        System.out.println("emailParam: " + emailParam);
        System.out.println("password: " + password);
        Optional<User> u = this.sqlDatabaseService.getDetailsByEmail(emailParam, password);
        User p = u.get();    
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getEmail());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserName());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getLocation());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserPicUrl());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserId());

        JsonObject payload = Json.createObjectBuilder()
                                .add("userId", p.getUserId())
                                .add("name", p.getUserName())
                                .add("location", p.getLocation())
                                .add("imageUrl", p.getUserPicUrl())   
                                .add("email", p.getEmail())
                                .build();
 
        System.out.println("payload: " + payload.toString());
        
        return ResponseEntity.ok(payload.toString());
    }

    @GetMapping(path="/get-details-by-userid/{userId}")
    public ResponseEntity<String> retrieveDetails(@PathVariable int userId){
        System.out.println("userId: " + userId);
        Optional<User> u = this.sqlDatabaseService.getDetailsByUserId(userId);
        User p = u.get();    
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getEmail());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserName());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getLocation());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserPicUrl());
        System.out.println("p>>>>>>>>>>>>>>>>>>>>>>>>>>>>.: " + p.getUserId());

        JsonObject payload = Json.createObjectBuilder()
                                .add("userId", p.getUserId())
                                .add("name", p.getUserName())
                                .add("location", p.getLocation())
                                .add("profilePicUrl", p.getUserPicUrl())   
                                .add("email", p.getEmail())
                                .add("lat", p.getLat())
                                .add("lng", p.getLng())
                                .build();
 
        System.out.println("payload: " + payload.toString());
        
        return ResponseEntity.ok(payload.toString());
    }

    @PostMapping(path = "/post-upload", consumes=MediaType.MULTIPART_FORM_DATA_VALUE, 
                                    produces= MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> postUpload(
        @RequestPart MultipartFile image,
        @RequestPart String userId,
        @RequestPart String title,
        @RequestPart String description,
        @RequestPart String category
        ) throws SQLException {
        try {
            String imageUrl = s3Svc.upload(image);
            Date postDate   = new Date(System.currentTimeMillis());
            int userIdInt = Integer.parseInt(userId);
            this.sqlDatabaseService.postUpload(userIdInt, title, description, category, imageUrl, postDate);
            System.out.println("imageUrl:>>>>>>>>> " + imageUrl);
            System.out.println("userId:>>>>>>>>> " + userId);
            System.out.println("title:>>>>>>>>> " + title);
            System.out.println("description:>>>>>>>>> " + description);
            System.out.println("category:>>>>>>>>> " + category);
            System.out.println("postDate:>>>>>>>>> " + postDate);
                       
            JsonObject payload = Json.createObjectBuilder()
            .add("imageKey", imageUrl)
            .add("userId", userId)
            .add("title", title)
            .add("description", description)
            .add("category", category)
            .add("postDate", postDate.toString())
            .build();
            return ResponseEntity.ok(payload.toString());

        } catch (IOException e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }

    @GetMapping(path="/get-posts")
    public ResponseEntity<String> retrievePosts() throws SQLException, IOException{
        List<Post> posts = this.sqlDatabaseService.getPosts();
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder();
        for (Post p : posts) {
            Optional<User> u = this.sqlDatabaseService.getLocationByUserId(p.getUserId());
            JsonObject payload = Json.createObjectBuilder()
                                .add("postId", p.getPostId())
                                .add("userId", p.getUserId())
                                .add("title", p.getTitle())
                                .add("description", p.getDescription())
                                .add("category", p.getCategory())
                                .add("imageUrl", p.getImageUrl())
                                .add("postDate", p.getPostDate().toString())
                                .add("location", u.get().getLocation())
                                .add("lat", u.get().getLat())
                                .add("lng", u.get().getLng())
                                .build();
            arrayBuilder.add(payload);
            System.out.println("payload: " + payload.toString());
        }
        JsonArray array = arrayBuilder.build();
        return ResponseEntity.ok(array.toString());
    }
    @GetMapping(path="/getLatNLng/{location}")
    public ResponseEntity<String> retrieveLatNLng(@PathVariable String location){
        System.out.println("location: " + location);
        double[] coordinates = googleMapService.getLatLng(location);
            double lat = coordinates[0];
            double lng = coordinates[1];
            System.out.println("lat: >>>>>" + lat);
            System.out.println("lng: >>>>>" + lng);

            JsonObject payload = Json.createObjectBuilder()
                                .add("lat", lat)
                                .add("lng", lng)
                                .build();

            return ResponseEntity.ok(payload.toString());
        
        
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<String> deletePost(@PathVariable int postId) {
    try {
        System.out.println("postId: >>>>>" + postId);
        boolean deleted = sqlDatabaseService.deletePost(postId);

        if (deleted) {
            return ResponseEntity.ok("Post deleted successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Post not found");
        }
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while deleting the post");
    }
}

}