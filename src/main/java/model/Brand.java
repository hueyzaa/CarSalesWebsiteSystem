package model;

import java.io.Serializable;

public class Brand implements Serializable {
    private static final long serialVersionUID = 1L;

    private int brandId;
    private String brandName;

    public Brand() {}

    public Brand(int brandId, String brandName) {
        this.brandId = brandId;
        this.brandName = brandName;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }
}