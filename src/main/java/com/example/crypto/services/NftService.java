package com.example.crypto.services;

import com.example.crypto.entities.NftEntity;
import com.example.crypto.entities.PreviousOwnerEntity;
import com.example.crypto.exceptions.NftNotFoundException;
import com.example.crypto.repositories.NftEntityRepository;
import com.example.crypto.security.model.User;
import com.example.crypto.security.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
        return nftEntityRepository.findAllByHidden(pageable, false);
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

            if (originalFileName == null || originalFileName.isEmpty()) {
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
        } else {
            throw new Exception("Попытка сохранить пустую nft");
        }
    }

    public Page<NftEntity> getAllNftCurrentUser(){
        User currentUser = userService.getCurrentUser();
        return null;


    }

    private NftEntity createNftEntity(NftDto nftDto, String UUID, String fileName) {
        NftEntity nft = new NftEntity();
        nft.setCreateDate(LocalDateTime.now());
        nft.setNftName(nftDto.getNftName());
        nft.setPicture(fileName);
        nft.setUniqNumber(UUID);
        nft.setHidden(true);
        nft.setDescription(nftDto.getDescription());
        nft.setCurrentOwner(userService.getCurrentUser());
        PreviousOwnerEntity previousOwnerEntity = PreviousOwnerEntity.builder().previousOwner(userService.getCurrentUser()).nftEntity(nft).number(1).startOfOwnership(nft.getCreateDate()).build();
        nft.getPreviousOwnerEntities().add(previousOwnerEntity);
        return nft;
    }

    public void updateNftDetails(NftEntity nftForUpdate, NftDto updatedValues) {
        List<String> changeList = new ArrayList<>();
        if (nftForUpdate.getHidden() != updatedValues.getHidden()) {
            nftForUpdate.setHidden(updatedValues.getHidden());
            changeList.add("hidden");
        }
        if (updatedValues.getAlias().isEmpty()) {
            nftForUpdate.setAlias(null);
            changeList.add("alias");
        } else if (!Objects.equals(updatedValues.getAlias(), nftForUpdate.getAlias())) {
            nftForUpdate.setAlias(updatedValues.getAlias());
            changeList.add("alias");
        }
        if (!Objects.equals(nftForUpdate.getNftName(), updatedValues.getNftName()) && !updatedValues.getNftName().isEmpty()) {
            nftForUpdate.setNftName(updatedValues.getNftName());
            changeList.add("nftName");
        }
        if (!Objects.equals(nftForUpdate.getDescription(), updatedValues.getDescription())) {
            nftForUpdate.setDescription(updatedValues.getDescription());
            changeList.add("description");
        }
        if (!Objects.equals(nftForUpdate.getInstantBuyPrice(), updatedValues.getInstantBuyPrice())) {
            nftForUpdate.setInstantBuyPrice(updatedValues.getInstantBuyPrice());
            changeList.add("instantBuyPrice");
        }
        nftForUpdate.setHidden(updatedValues.getHidden());
        nftEntityRepository.save(nftForUpdate);
    }

    @Transactional
    public NftEntity instantBuyNft(String numberOrAlias, User buyer) throws NftNotFoundException {
        NftEntity nft = getNftByUniqNumberOrAlias(numberOrAlias);
        if (nft.getCurrentOwner().equals(buyer)) {
            return null;
        }
        Double buyerBalance = buyer.getBalance();
        Double nftPrice = nft.getInstantBuyPrice();
        if (nftPrice == null) {
            return null;
        }

        if (buyerBalance < nftPrice) {
            return null;

        }

        LocalDateTime now = LocalDateTime.now();
        User nftOwner = nft.getCurrentOwner();
        List<PreviousOwnerEntity> previousOwnerEntities = nft.getPreviousOwnerEntities();
        PreviousOwnerEntity previousOwnerEntity = previousOwnerEntities.stream().max(Comparator.comparingInt(PreviousOwnerEntity::getNumber)).get();
        previousOwnerEntity.setFinishOfOwnership(now);
        previousOwnerEntity.setSellingPrice(nftPrice);

        Integer lastNumber = previousOwnerEntity.getNumber();
        PreviousOwnerEntity currentPreviousOwnerEntity = PreviousOwnerEntity.builder().previousOwner(buyer).nftEntity(nft).number(lastNumber + 1).startOfOwnership(now).purchasePrice(nftPrice).build();
        previousOwnerEntities.add(currentPreviousOwnerEntity);
        nft.setPreviousOwnerEntities(previousOwnerEntities);
        nftOwner.setBalance(nftOwner.getBalance() + nftPrice);
        buyer.setBalance(buyer.getBalance() - nftPrice);

        nft.setCurrentOwner(buyer);
        nft.setInstantBuyPrice(null);
        nft.setHidden(false);


        userService.updateUser(buyer);
        userService.updateUser(nftOwner);

        return nftEntityRepository.save(nft);
    }
}
