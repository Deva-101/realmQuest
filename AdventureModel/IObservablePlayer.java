package AdventureModel;

/**
 * An interface that allows observers to
 * subscribe to this player to update when the player's
 * stats update.
 */
public interface IObservablePlayer {
    void registerObserver(IPlayerObserver o);
    void dropObserver(IPlayerObserver o);
    void notifyObservers();
}
