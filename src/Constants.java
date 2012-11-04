class Constants {
    public static final double  frictionFactor     = 0.995;
    public static final double  regularShellSpeed  = 16.7;
    public static final double  premiumShellSpeed  = 13.3;
    public static final double  dangerousAngle     = 4.0 / 180.0 * Math.PI;
    public static final double  smallDistance      = 20.0;
    public static final int[][] directions         = {{1, 0}, {0, 1}, {-1, 0}, {0, -1},};
    public static final double  tileSize           = 25.0;
    public static final double  defaultPriority    = 0.5;
    public static final int     predictionTickTime = 100;

    public static final double goodAngle = 3.0 / 180.0 * Math.PI;

    private Constants() {}
}
