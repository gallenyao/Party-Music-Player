package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.HostRole}. Used by guest's of the party to find the party/party-host
 *
 * @author Rishabh Gulati
 */
public class IdentifyHost extends Event {
    //public static final String TOPIC = IdentifyHost.class.getCanonicalName();

    public static final String TOPIC = IdentifyHost.class.getCanonicalName();

    public IdentifyHost() {
        super(Flag.REQUEST, TOPIC);
    }
}
