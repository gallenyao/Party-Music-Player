package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.Message;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.GuestRole}. Used by host of the party to indicate the end of the party.
 *
 * @author Litianlong Yao
 */
public class End extends Event {
    //public static final String TOPIC = End.class.getCanonicalName();

    public static final String TOPIC = End.class.getCanonicalName();


    public End() {
        super(Message.Flag.NOTICE, TOPIC);
    }
}
