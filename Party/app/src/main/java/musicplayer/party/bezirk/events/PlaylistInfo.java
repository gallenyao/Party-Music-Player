package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.GuestRole}. Used by host of the party to send playlist information to guests.
 *
 * @author Rishabh Gulati
 */
public class PlaylistInfo extends Event {
//    public static final String TOPIC = PlaylistInfo.class.getCanonicalName();

    public static final String TOPIC = PlaylistInfo.class.getCanonicalName();
    public PlaylistInfo() {
        super(Flag.NOTICE, TOPIC);
    }
}
