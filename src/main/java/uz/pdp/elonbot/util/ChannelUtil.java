package uz.pdp.elonbot.util;

import lombok.Getter;

@Getter
public class ChannelUtil {

    @Getter
    private static String channelUsername;

    public static void setChannelUsername(String channelUsername) {
        ChannelUtil.channelUsername = channelUsername;
    }

}
