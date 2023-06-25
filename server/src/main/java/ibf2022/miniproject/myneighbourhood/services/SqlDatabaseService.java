package ibf2022.miniproject.myneighbourhood.services;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
// import org.springframework.web.multipart.MultipartFile;

import ibf2022.miniproject.myneighbourhood.model.User;
import ibf2022.miniproject.myneighbourhood.repositories.UserRepository;



@Service
public class SqlDatabaseService {
    @Autowired
    private UserRepository userRepo;

    public void upload(String userName, String location, Double lat, Double lng, 
    String email, Boolean isEmailVerified, String password, String userPicUrl, Date joinDate) 
    throws SQLException, IOException {
        this.userRepo.signUp(userName, location, lat, lng, email, isEmailVerified, password, userPicUrl, joinDate);
    }

    public Optional<User> getDetailsByEmail(String email, String password){
        System.out.println("emailParam at sql: " + email);
        System.out.println("password at sql: " + password);
        return this.userRepo.getDetailsByEmail(email, password);
    }

    public Optional<User> getDetailsByUserId(int userId){
        return this.userRepo.getDetailsByUserId(userId);
        
    }
}