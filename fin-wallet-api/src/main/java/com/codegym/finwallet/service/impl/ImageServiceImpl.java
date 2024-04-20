package com.codegym.finwallet.service.impl;

import ch.qos.logback.classic.encoder.JsonEncoder;
import com.codegym.finwallet.constant.VarConstant;
import com.codegym.finwallet.dto.CommonResponse;
import com.codegym.finwallet.dto.payload.response.ProfileResponse;
import com.codegym.finwallet.entity.Profile;
import com.codegym.finwallet.repository.ProfileRepository;
import com.codegym.finwallet.service.ImageService;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final ModelMapper modelMapper;
    private final ProfileRepository profileRepository;
    private final String UPDATE_PROFILE_AVATAR_SUCCESS_MESSAGE = VarConstant.UPDATE_PROFILE_AVATAR_SUCCESS;
    private final String UPDATE_PROFILE_AVATAR_FAIL_MESSAGE = VarConstant.UPDATE_PROFILE_AVATAR_FAIL;

    @Override
    public String uploadFile(File file, String fileName) throws IOException {
        BlobId blobId = BlobId.of("fin-wallet-ee07d.appspot.com", fileName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId).setContentType("media").build();
        InputStream inputStream = ImageService.class.getClassLoader().getResourceAsStream("firebase-private-key.json");
        Credentials credentials = GoogleCredentials.fromStream(inputStream);
        Storage storage = StorageOptions.newBuilder().setCredentials(credentials).build().getService();
        storage.create(blobInfo, Files.readAllBytes(file.toPath()));

        String DOWNLOAD_URL = "https://firebasestorage.googleapis.com/v0/b/fin-wallet-ee07d.appspot.com/o/%s?alt=media";
        return String.format(DOWNLOAD_URL, URLEncoder.encode(fileName, StandardCharsets.UTF_8));
    }

    @Override
    public File convertToFile(MultipartFile multipartFile, String fileName) throws IOException {
        File tempFile = new File(fileName);
        try (FileOutputStream fos = new FileOutputStream(tempFile)) {
            fos.write(multipartFile.getBytes());
        }
        return tempFile;
    }

    @Override
    public String getExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    @Override
    public CommonResponse upload(MultipartFile multipartFile) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        Optional<Profile> profileOptional = profileRepository.findProfileByEmail(email);
        Profile profile = new Profile();
        ProfileResponse profileResponse = new ProfileResponse();
        try {

            String fileName = multipartFile.getOriginalFilename();
            fileName = UUID.randomUUID().toString().concat(this.getExtension(fileName));

            File file = this.convertToFile(multipartFile, fileName);
            String URL = this.uploadFile(file, fileName);
            file.delete();
            if (profileOptional.isPresent()){
                profile = profileOptional.get();
                profile.setImageUrl(URL);
                profileRepository.save(profile);

                profileResponse = modelMapper.map(profile, ProfileResponse.class);
            }
            return CommonResponse.builder()
                    .data(profileResponse)
                    .message(UPDATE_PROFILE_AVATAR_SUCCESS_MESSAGE)
                    .status(HttpStatus.OK)
                    .build();


        } catch (Exception e) {
            return CommonResponse.builder()
                    .data(null)
                    .message(UPDATE_PROFILE_AVATAR_FAIL_MESSAGE)
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
    }
}
