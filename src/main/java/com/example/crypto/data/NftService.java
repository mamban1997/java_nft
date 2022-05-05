package com.example.crypto.data;

import com.example.crypto.exceptions.NftNotFoundException;
import com.example.crypto.security.service.UserService;
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

    public NftEntity safeNewNft(NftDto nftDto) throws Exception {
        MultipartFile file = nftDto.getFile();
        if (!file.isEmpty()) {
            byte[] bytes = file.getBytes();
            String UUID = NftHelper.generateGuid();
            String originalFileName = file.getOriginalFilename();

            if (originalFileName==null || originalFileName.isEmpty()){
                System.out.println("Ошибка файл без имени");
                return null;
            }

            String fileName = UUID + originalFileName.substring(originalFileName.lastIndexOf('.'));
            File dir = new File(nftImagePath + File.separator + "loadFiles");
            if (!dir.exists()) {
                //noinspection ResultOfMethodCallIgnored
                dir.mkdirs();
            }
            File uploadedFile = new File(dir.getAbsolutePath() + File.separator + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(uploadedFile));
            stream.write(bytes);
            stream.flush();
            stream.close();

            NftEntity nft = createNftEntity(nftDto, UUID, fileName);
            nftEntityRepository.save(nft);
            return nft;
        }else{
            throw new Exception("Попытка сохранить пустую nft");
        }
    }

    private NftEntity createNftEntity(NftDto nftDto, String UUID, String fileName) {
        NftEntity nft = new NftEntity();
        nft.setCreateDate(LocalDateTime.now());
        nft.setNftName(nftDto.getNftName());
        nft.setPicture(fileName);
        nft.setUniqNumber(UUID);
        nft.setDescription(nftDto.getDescription());
        nft.setCurrentOwner(userService.getCurrentUser());
        PreviousOwner previousOwner = PreviousOwner.builder().previousOwner(userService.getCurrentUser()).nftEntity(nft).number(1).startOfOwnership(nft.getCreateDate()).build();
        nft.getPreviousOwners().add(previousOwner);
        return nft;
    }

    public boolean updateNftDetails(NftEntity nftForUpdate, NftDto updatedValues) {
nftForUpdate.setHidden(updatedValues.getHidden());
nftEntityRepository.save(nftForUpdate);
        return true;
    }
}
