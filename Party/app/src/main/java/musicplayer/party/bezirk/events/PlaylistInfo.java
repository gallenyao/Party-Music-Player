package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;

import musicplayer.party.helper.PartyConstant;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.GuestRole}. Used by host of the party to send playlist information to guests.
 *
 * @author Rishabh Gulati
 */
public class PlaylistInfo extends Event {
//    public static final String TOPIC = PlaylistInfo.class.getCanonicalName();

    public static final String TOPIC = PlaylistInfo.class.getCanonicalName();
    //public static final String TOPIC = PartyConstant.partyPlaylistID;

    public String ppid = PartyConstant.partyPlaylistID;

    public PlaylistInfo() {
        super(Flag.NOTICE, TOPIC);
    }
}
