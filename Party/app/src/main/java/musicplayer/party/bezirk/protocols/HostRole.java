package musicplayer.party.bezirk.protocols;

import com.bezirk.middleware.messages.ProtocolRole;

import musicplayer.party.bezirk.events.IdentifyHost;
import musicplayer.party.bezirk.events.Join;
import musicplayer.party.bezirk.events.SharePreferences;

/**
 * @author Litianlong Yao
 */
public class HostRole extends ProtocolRole {
    private static final String roleName = "Host Role";
    private static final String roleDescription = "Role used by host in the party app";
    private static final String[] topics = {IdentifyHost.TOPIC, Join.TOPIC, SharePreferences.TOPIC};

    private static final String[] streamTopics = {"test host", "---test host"};

    @Override
    public String getRoleName() {
        return roleName;
    }

    @Override
    public String getDescription() {
        return roleDescription;
    }

    @Override
    public String[] getEventTopics() {
        return topics;
    }

    @Override
    public String[] getStreamTopics() {
        return streamTopics;
        //return new String[0];
    }
}
