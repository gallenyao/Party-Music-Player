package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.HostRole}. Used by guest's of the party to accept the invite for the party issue by the Host using {@link Invite}
 *
 * @author Litianlong Yao
 */
public class Join extends Event {

    public static final String TOPIC = Join.class.getCanonicalName();

    public Join() {
        super(Flag.REPLY, TOPIC);
    }
}
