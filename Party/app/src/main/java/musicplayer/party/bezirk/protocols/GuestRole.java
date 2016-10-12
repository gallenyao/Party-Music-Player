package musicplayer.party.bezirk.protocols;

import com.bezirk.middleware.messages.ProtocolRole;

import musicplayer.party.bezirk.events.End;
import musicplayer.party.bezirk.events.Invite;
import musicplayer.party.bezirk.events.PlaylistInfo;
import musicplayer.party.bezirk.events.PreferencesAccepted;

/**
 * @author Litianlong Yao
 */
public class GuestRole extends ProtocolRole {
    private static final String roleName = "Guest role";
    private static final String roleDescription = "Role used by guests in the party app";
    private static final String[] topics = {Invite.TOPIC, PlaylistInfo.TOPIC, PreferencesAccepted.TOPIC, End.TOPIC};

    private static final String[] streamTopics = {"test guest", "---test guest"};

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
