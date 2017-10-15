package edu.csulb.smartroot.gardenview;

/**
 * Contains garden card data. Is used to extract data to display in the card view.
 */
public class Garden {
    private String gardenName;
    private String macAddress;

    private double temperature;
    private double moisture;
    private double humidity;
    private int lastUpdated;

    /**
     * Constructor that initializes the garden name and sensor readings to default values.
     * @param pos The position of the garden in the RecyclerView. For testing purposes and will
     *            be changed in the future.
     */
    public Garden(int pos) {
        this.gardenName = "Garden " + pos;
        this.macAddress = "";
        this.temperature = 0;
        this.moisture = 0;
        this.humidity = 0;
        this.lastUpdated = 0;
    }

    /**
     * Constructor that receives the garden name and MAC address. Initializes the sensor readings
     * to default values.
     * @param gardenName The name of the garden.
     * @param macAddress The MAC address of the garden.
     */
    public Garden(String gardenName, String macAddress) {
        this.gardenName = gardenName;
        this.macAddress = macAddress;

        this.temperature = 0;
        this.temperature = 0;
        this.moisture = 0;
        this.humidity = 0;
        this.lastUpdated = 0;
    }

    /**
     * Gets the name of the garden.
     * @return Name of the garden.
     */
    public String getGardenName() {
        return gardenName;
    }

    /**
     * Gets the mac address of the garden.
     * @return mac address of the garden.
     */
    public String getMacAddress() {
        return macAddress;
    }

    /**
     * Gets the temperature of the garden.
     * @return Temperature of garden.
     */
    public double getTemperature() {
        return temperature;
    }

    /**
     * Gets the moisture of the garden.
     * @return Moisture of the garden.
     */
    public double getMoisture() {
        return moisture;
    }

    /**
     * Gets the humidity of the garden.
     * @return Humidity of the garden.
     */
    public double getHumidity() {
        return humidity;
    }

    /**
     * Gets the last time the garden sensor readings have been updated.
     * @return The last time the sensor readings have been updated.
     */
    public int getLastUpdated() {
        return lastUpdated;
    }

    /**
     * Sets the garden name.
     * @param gardenName The garden name.
     */
    public void setGardenName(String gardenName) {
        this.gardenName = gardenName;
    }

    /**
     * Sets the garden temperature.
     * @param temperature The garden temperature.
     */
    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    /**
     * Sets the garden moisture level.
     * @param moisture The garden moisture level.
     */
    public void setMoisture(double moisture) {
        this.moisture = moisture;
    }

    /**
     * Sets the garden humidity level.
     * @param humidity The garden humidity level.
     */
    public void setHumidity(double humidity) {
        this.humidity = humidity;
    }

    /**
     * Sets the last time the garden sensor readings have been updated.
     * @param lastUpdated The last time garden sensor readings have been updated.
     */
    public void setLastUpdated(int lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
