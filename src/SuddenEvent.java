public enum SuddenEvent
{
    HIPPOGRIF, BERTIEBEANS, FEAST, DEMENTOR;
    @Override
    public String toString()
    {
        return name().charAt(0)+name().substring(1).toLowerCase();
    }
}
