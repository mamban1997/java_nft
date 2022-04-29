package com.example.crypto.data;

import com.example.crypto.exceptions.NftNotFoundException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public NftService(NftEntityRepository nftEntityRepository) {
        this.nftEntityRepository = nftEntityRepository;
    }

    public Page<NftEntity> getPageOfNftEntity(int size, int offset) {
        Pageable pageable = PageRequest.of(size, offset);
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
            File uploadedFile = new File(dir.getAbsolutePath() + File.separator + name);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
            stream.write(bytes);
            stream.flush();
            stream.close();

            NftEntity nft = new NftEntity();
            nft.setCreateDate(LocalDateTime.now());
            nft.setNftName(nftDto.getName());
            nft.setPicture(name);
            nft.setUniqNumber(name);

            nftEntityRepository.save(nft);
        }
    }

}
