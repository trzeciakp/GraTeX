package pl.edu.agh.gratex.model.properties;

/**
 *
 */
public enum IsLoop implements Emptible {
    EMPTY {

        @Override
        public boolean isEmpty() {
            return true;
        }
    }, YES, NO;
    @Override
    public boolean isEmpty() {
        return false;
    }
}
