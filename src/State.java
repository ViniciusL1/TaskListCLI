public enum State {
    TODO,
    IN_PROGRESS,
    DONE;

    public static State fromString(String text) {
        try {
            return State.valueOf(text.toUpperCase().replace("-", "_"));
        } catch (IllegalArgumentException e) {
            return TODO; // Valor padrão
        }
    }

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", "-");
    }
}