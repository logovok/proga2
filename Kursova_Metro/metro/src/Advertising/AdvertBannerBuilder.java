package Advertising;

import java.util.Optional;

public class AdvertBannerBuilder {
    private String ownerOfBanner;
    private String mainTitle;
    private String tagline;
    private Optional<String> optionalInformation;

    public AdvertBannerBuilder owner(String ownerOfBanner) {
        this.ownerOfBanner = ownerOfBanner;
        return this;
    }

    public AdvertBannerBuilder title(String mainTitle) {
        this.mainTitle = mainTitle;
        return this;
    }

    public AdvertBannerBuilder tagline(String tagline) {
        this.tagline = tagline;
        return this;
    }

    public AdvertBannerBuilder optionalInformation(String optionalInformation) {
        this.optionalInformation = Optional.ofNullable(optionalInformation);
        return this;
    }

    public AdvertBanner build() throws IllegalStateException {
        AdvertBanner banner = new AdvertBanner();
        if ((ownerOfBanner == null) || (mainTitle == null) || (tagline == null))
            throw new IllegalStateException("Not all fields were built");
        banner.ownerOfBanner = ownerOfBanner;
        banner.mainTitle = mainTitle;
        banner.tagline = tagline;
        banner.optionalInformation = optionalInformation;
        return banner;
    }
}
