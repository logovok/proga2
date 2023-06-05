package Advertising;

import java.util.Optional;

public class AdvertBanner implements Cloneable {
    String ownerOfBanner;
    String mainTitle;
    String tagline;
    Optional<String> optionalInformation = Optional.empty();

    AdvertBanner() {}

    AdvertBanner(AdvertBanner ab){
        ownerOfBanner =ab.ownerOfBanner;
        mainTitle = ab.mainTitle;
        tagline =ab.tagline;
        if (!optionalInformation.isEmpty()){
            optionalInformation = Optional.of(optionalInformation.get());
        }

    }

    @Override
    public AdvertBanner clone() {

        return new AdvertBanner(this);
    }


    @Override
    public String toString(){
        var sb = new StringBuilder()
                .append("<owner>").append(ownerOfBanner).append("<\\owner>\n")
                .append("<main-title>").append(mainTitle).append("<\\main-title>\n")
                .append("<tagline>").append(tagline).append("<\\tagline>\n");

        return optionalInformation.map(s -> sb  .append("<optional>")
                                                .append(s)
                                                .append("<\\optional>\n")
                                                .toString())
                .orElseGet(sb::toString);
    }
}

