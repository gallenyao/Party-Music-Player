package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

import musicplayer.party.helper.PartyConstant;
import musicplayer.party.helper.PersonalizationConstant;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.HostRole}. Used by guest's of the party to find the party/party-host
 *
 * @author Litianlong Yao
 */
public class IdentifyHost extends Event {

    public static final String TOPIC = IdentifyHost.class.getCanonicalName();

    public IdentifyHost() {
        super(Flag.REQUEST, TOPIC);
    }
}
