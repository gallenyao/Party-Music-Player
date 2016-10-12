package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.GuestRole}. Used by guest's of the party to find the party/party-host
 *
 * @author Litianlong Yao
 */
public class Invite extends Event {

    public static final String TOPIC = Invite.class.getCanonicalName();

    public Invite() {
        super(Flag.REQUEST, TOPIC);
    }
}
