package com.example.crypto.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NftNotFoundException extends Exception {
    private String numberOrAlias;
    private boolean isAlias;
    public NftNotFoundException(String numberOrAlias, boolean isAlias) {
        this.numberOrAlias = numberOrAlias;
        this.isAlias = isAlias;
    }

}
