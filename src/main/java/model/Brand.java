package model;

public class Brand {
    private int brandId;
    private String brandName;
    private String brandCountry;

    // Constructors
    public Brand() {}

    public Brand(int brandId, String brandName, String brandCountry) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.brandCountry = brandCountry;
    }

    // Getters and Setters
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
    public String getBrandCountry() {
        return brandCountry;
    }
    public void setBrandCountry(String brandCountry) {
        this.brandCountry = brandCountry;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                '}';
    }
}