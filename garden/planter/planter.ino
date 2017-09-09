/**
 * \file planter.ino
 * \brief This is the planter. It handles reading sensors and powering the water pump when needed
 */

#include <Wire.h>

#include "moisture.h"
#include "dht.h"

#define DEBUG 0
#define I2C_CHANNEL 1

//////////////////
// DIGITAL PINS //
//////////////////
#define TEMP_HUMID_UNIT1_PIN 2
#define TEMP_HUMID_UNIT2_PIN 4
#define MOISTURE_POWER_PIN 13
#define WATER_FLOW_PIN 7

/////////////////
// ANALOG PINS //
/////////////////
#define MOISTURE_PIN 0

//////////////////
// DELAY TIMERS //
//////////////////
#define UPDATE_RATE 1 // in minutes

// Define sensors
dht TempHumidSensor1;
dht TempHumidSensor2;

moisture Moisture(MOISTURE_PIN, MOISTURE_POWER_PIN, 600);

const unsigned long updateTime = long(UPDATE_RATE) * 60 * 1000;

// Initialize sensor readings
double tempAvg = 0.0;

double humidAvg = 0.0;
double moisture = 0.0;

boolean isOn = false;

/**
 * \brief Initializes sensors, water flow relay, and i2c communication
 */
void setup() {
  Serial.begin(9600);

  // Initialize the Temperature and Humidity sensors
  Serial.println("Initializing...");
  Serial.print("Temperature and Humidity...");
  int chk = TempHumidSensor1.read11(TEMP_HUMID_UNIT1_PIN);
  if (chk == DHTLIB_OK)
    Serial.println("Unit 1 OK!");
  else    
    Serial.println("Unit 1 ERROR!");

  chk = TempHumidSensor2.read11(TEMP_HUMID_UNIT2_PIN);
  if (chk == DHTLIB_OK)
    Serial.println("Unit 2 OK!");
  else
    Serial.println("Unit 2 ERROR!");

  // Initialize water flow signal and relay
  Serial.print("Water Flow Signal...");
  pinMode(WATER_FLOW_PIN, OUTPUT);
  Serial.println("OK!");

  // Initialize i2c communication with WiFi Module
  Serial.print("WiFi Module...");
  Wire.begin(I2C_CHANNEL);
  Wire.onRequest(sendReadings);
  Serial.println("OK!");
}

/**
 * \brief Gets sensor readings and turn on water flow based on predefined threshold
 */
void loop() {
  readSensors(tempAvg, humidAvg, moisture);
  
  if (moisture <= 60.0)
    waterFlow(true);
  else
    waterFlow(false);

  Serial.print("Delaying ");
  Serial.print(updateTime);
  Serial.print("...");
  delay(updateTime);
  Serial.println("done!");
}

/**
 * \brief A toggle for the water flow. It is either on or off.
 */
void waterFlow(boolean flow) {
  if (flow == true)
    digitalWrite(WATER_FLOW_PIN, HIGH);
  else
    digitalWrite(WATER_FLOW_PIN, LOW);
}

#if DEBUG
  void waterFlowTest(){
    if (isOn)
      digitalWrite(WATER_FLOW_PIN, HIGH);
    else
      digitalWrite(WATER_FLOW_PIN, LOW);
  
    isOn = !isOn;
  }
#endif

/**
 * \brief Reads the current sensor readings and prints out the result
 */
void readSensors(double &_temperature, double &_humidity, double &_moisture){
  double tempUnit1 = 0.0;
  double humidUnit1 = 0.0;

  double tempUnit2 = 0.0;
  double humidUnit2 = 0.0;
  
  // Update unit 1 temeperature and humidity
  TempHumidSensor1.read11(TEMP_HUMID_UNIT1_PIN);
  tempUnit1 = celsiusToFahrenheit(TempHumidSensor1.temperature);
  humidUnit1 = TempHumidSensor1.humidity;

  // Update unit 2 temperature
  TempHumidSensor2.read11(TEMP_HUMID_UNIT2_PIN);
  tempUnit2 = celsiusToFahrenheit(TempHumidSensor2.temperature);
  humidUnit2 = TempHumidSensor2.humidity;

  // Get moisture readings
  Moisture.read();

  _temperature = (tempUnit1 + tempUnit2) / 2.0;
  _humidity = (humidUnit1 + humidUnit2) / 2.0;
  _moisture = Moisture.getReadings();

  #if DEBUG
    printSensors(_temperature, _humidity, _moisture, Moisture.getRawReadings(), tempUnit1, tempUnit2, humidUnit1, humidUnit2);
  #else
    printSensors(_temperature, _humidity, _moisture);
  #endif
}

/**
 * \brief A function that is called when bytes are requested from the WiFi module through i2c
 */
void sendReadings(){
  i2cSend(tempAvg);
  i2cSend(humidAvg);
  i2cSend(moisture);
}

/**
 * \brief A helper function that sends sensor readings to the WiFi Module through i2c
 *
 * i2c sends data a byte at a time and the data type cannot be double or float,
 * so bit and decimal manipulation is needed. The sensor readings are multipled by 100
 * and is sent as an int with a length of 2 bytes. The first write is the high byte
 * of the readings. The second write is the low byte of the readings. The sensor readings
 * are divided by 100.0 by the WiFi Module to get the original result.
 */
void i2cSend(double reading){
  int toSend = reading * 100;

  byte toSendHigh = highByte(toSend);
  byte toSendLow = lowByte(toSend);

  Wire.write(toSendHigh);
  Wire.write(toSendLow);
}

/**
 * \brief A helper function that converts celsius to fahrenheit
 *
 * @return A the fahrenheit conversion of the temperature
 */
double celsiusToFahrenheit(double celsius){
  return celsius * 9 / 5 + 32;
}

#if DEBUG
  /**
   * \brief Prints the temperature, humidity, and moisture sensor readings for debugging purposes
   */
  void printSensors(double &_temperature, double &_humidity, double &_moisture, double &_moistureValue, double &_tempUnit1, double &_tempUnit2, double &_humidUnit1, double &_humidUnit2){
    Serial.print("Temperature: ");
    Serial.print(_temperature);
    Serial.print(" | Humidity: ");
    Serial.print(_humidity);
    Serial.print(" | Moisture: ");
    Serial.print(_moisture);
    Serial.print(" | Moisture Value: ");
    Serial.print(_moistureValue);
    Serial.print(" | Temp Unit 1: ");
    Serial.print(_tempUnit1);
    Serial.print(" | Humid Unit 1: ");
    Serial.print(_humidUnit1);
    Serial.print(" | Temp Unit 2: ");
    Serial.print(_tempUnit2);
    Serial.print(" | Humid Unit 2: ");
    Serial.println(_humidUnit2);
    Serial.println();
  }
#else
  /**
   * \brief Prints the temperature, humidity, and moisture sensor readings for debugging purposes
   */
  void printSensors(double &_temperature, double &_humidity, double &_moisture){
    Serial.print("Temperature: ");
    Serial.print(_temperature);
    Serial.print(" | Humidity: ");
    Serial.print(_humidity);
    Serial.print(" | Moisture: ");
    Serial.println(_moisture);
  }
#endif
