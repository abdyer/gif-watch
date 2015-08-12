package org.andydyer.gifwatch.api;

/**
 * Created by andy on 8/11/15.
 */
public class Data {

    /*
    data: {
        type: "gif",
        id: "109b9ic41IpFPG",
        url: "http://giphy.com/gifs/mtv-80s-109b9ic41IpFPG",
        image_original_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/giphy.gif",
        image_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/giphy.gif",
        image_mp4_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/giphy.mp4",
        image_frames: "10",
        image_width: "400",
        image_height: "300",
        fixed_height_downsampled_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/200_d.gif",
        fixed_height_downsampled_width: "267",
        fixed_height_downsampled_height: "200",
        fixed_width_downsampled_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/200w_d.gif",
        fixed_width_downsampled_width: "200",
        fixed_width_downsampled_height: "150",
        fixed_height_small_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/100.gif",
        fixed_height_small_still_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/100_s.gif",
        fixed_height_small_width: "133",
        fixed_height_small_height: "100",
        fixed_width_small_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/100w.gif",
        fixed_width_small_still_url: "http://s3.amazonaws.com/giphygifs/media/109b9ic41IpFPG/100w_s.gif",
        fixed_width_small_width: "100",
        fixed_width_small_height: "75"
    }
    */

    private String fixedWidthDownsampledUrl; // Wear requires files 100KB or less

    public String getFixedWidthDownsampledUrl() {
        return fixedWidthDownsampledUrl;
    }
}
