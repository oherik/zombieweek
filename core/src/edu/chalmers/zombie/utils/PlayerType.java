package edu.chalmers.zombie.utils;

/**
 * Created by neda on 2015-05-28.
 * Modified by Tobias.
 */
public enum PlayerType {

    EMIL("emil","emil-still","emil-hand"),
    EMILIA("emilia","emilia-still","emilia-hand");

    private String imageAnimatedKey;
    private String imageOverlayKey;
    private String imageStandingStillKey;

    private PlayerType(String imageAnimatedKey, String imageStandingStillKey, String imageOverlayKey){
        this.imageAnimatedKey = imageAnimatedKey;
        this.imageOverlayKey = imageOverlayKey;
        this.imageStandingStillKey = imageStandingStillKey;
    }

    public String getImageStandingStillKey() {
        return imageStandingStillKey;
    }

    public String getImageOverlayKey() {
        return imageOverlayKey;
    }

    public String getImageAnimatedKey() {
        return imageAnimatedKey;
    }
}
