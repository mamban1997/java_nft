package com.example.crypto.data;

import com.example.crypto.exceptions.NftNotFoundException;
import com.example.crypto.security.service.UserService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class NftService {

    @Value("${nft.imagePath}")
    private String nftImagePath;

    private final NftEntityRepository nftEntityRepository;
    private final UserService userService;

    public NftService(NftEntityRepository nftEntityRepository, UserService userService) {
        this.nftEntityRepository = nftEntityRepository;
        this.userService = userService;
    }

    public Page<NftEntity> getPageOfNftEntity(int size, int offset) {
        Pageable pageable = PageRequest.of(size, offset, Sort.by(Sort.Direction.DESC, "createDate"));
        return nftEntityRepository.findAll(pageable);
    }

    public NftEntity getNftByUniqNumberOrAlias(String guidOrAlias) throws NftNotFoundException {
        NftEntity nft;
        boolean isAlias = false;
        if (NftHelper.isGuid(guidOrAlias)) {
            //this is number
            nft = nftEntityRepository.findByUniqNumber(guidOrAlias);
        } else {
            //this is alias
            isAlias = true;
            nft = nftEntityRepository.findByAlias(guidOrAlias);
        }
        if (nft != null) {
            return nft;
        } else {
            throw new NftNotFoundException(guidOrAlias, isAlias);
        }
    }

    public void safeNewNft(NftDto nftDto) throws IOException {
        MultipartFile file = nftDto.getFile();
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String name = NftHelper.generateGuid();
            File dir = new File(nftImagePath + File.separator + "loadFiles");
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name+ file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
            stream.write(bytes);
            stream.flush();
            stream.close();

            NftEntity nft = new NftEntity();
            nft.setCreateDate(LocalDateTime.now());
            nft.setNftName(nftDto.getName());
            nft.setPicture(name + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.')));
            nft.setUniqNumber(name);
            nft.setDescription(nftDto.getDescription());
            nft.setCurrentOwner(userService.getCurrentUser());
            nftEntityRepository.save(nft);
        }
    }

}
