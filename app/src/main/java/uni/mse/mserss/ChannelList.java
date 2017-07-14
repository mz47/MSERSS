package uni.mse.mserss;

import java.util.ArrayList;

/**
 * Created by marcel on 22.05.17.
 */

public class ChannelList {

    private ArrayList<Channel> channels;

    public ChannelList() {
        channels = new ArrayList<>();
    }

    public void add(Channel channel) {
        if(channel != null) {
            channels.add(channel);
        }
    }

    public Channel get(int position) {
        if(channels != null && position >= 0) {
            return channels.get(position);
        }
        return null;
    }

    public ArrayList<Channel> getChannels() {
        return channels;
    }

    public ArrayList<String> toTitleList() {
        if(channels != null) {
            ArrayList<String> titles = new ArrayList<>();
            for(Channel c : channels) {
                titles.add(c.getTitle());
            }
            return titles;
        }
        return null;
    }
}
