package AdventureModel;

/**
 * The view will be subscribed to the Player
 * so that on player information change, then
 * the view can update accordingly.
 */
public interface IPlayerObserver {
    void updatePlayer();
}
