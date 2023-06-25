package ibf2022.miniproject.myneighbourhood.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.joda.time.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;



import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
    
    @Autowired
    private AmazonS3 s3Client;

    @Value("${DO_STORAGE_BUCKETNAME}")
    private String bucketName;

    
    public String upload(MultipartFile file) throws IOException{
        Map<String, String> userData = new HashMap<>();
        userData.put("name", "myneighbourhood");
        userData.put("uploadDateTime", LocalDateTime.now().toString());
        userData.put("originalFilename", file.getOriginalFilename());
        
    ObjectMetadata metadata = new ObjectMetadata();
    metadata.setContentType("application/octet-stream");
    metadata.setContentLength(file.getSize());
    metadata.setUserMetadata(userData);

    String key = UUID.randomUUID().toString().substring(0, 8);

    PutObjectRequest putRequest = new PutObjectRequest(
        bucketName,
        "myobject%s.png".formatted(key),
        file.getInputStream(),
        metadata
    );
    putRequest.withCannedAcl(CannedAccessControlList.PublicRead);
    s3Client.putObject(putRequest);
        
    return getImageUrl(key);
    }   
    
    private String getImageUrl(String key) {
        return String.format("https://%s.sgp1.cdn.digitaloceanspaces.com/myobject%s.png", bucketName, key);
    }
}