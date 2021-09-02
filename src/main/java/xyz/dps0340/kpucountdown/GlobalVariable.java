package xyz.dps0340.kpucountdown;

public abstract class GlobalVariable {
    public static final String VACCINE_OPENAPI_SERVICE_KEY = System.getenv("VACCINE_OPENAPI_SERVICE_KEY");
    public static final String OPENAPI_ENDPOINT = "https://api.odcloud.kr/api/15077756/v1/vaccine-stat";
    public static final int NATIONAL_POPULATION = 51672400;
}
