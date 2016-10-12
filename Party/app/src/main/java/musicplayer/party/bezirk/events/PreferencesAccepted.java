package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.Message;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.GuestRole}. Used by host of the party to indicate preferences shared using {@link SharePreferences} are accepted
 * @author Litianlong Yao
 */
public class PreferencesAccepted extends Event {

    public static final String TOPIC = PreferencesAccepted.class.getCanonicalName();
    public PreferencesAccepted() {
        super(Message.Flag.NOTICE, TOPIC);
    }
}
