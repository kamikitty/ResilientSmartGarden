/**
 * \file moisture.cpp
 * \brief Moisture sensor logic
 */

#include "moisture.h"

/**
 * A constructor that will get the moisture sensor pin for readings, pin for power,
 * and the max raw analog value reading.
 * 
 * @param[in] _pin The analog pin of the moisture sensor.
 * @param[in] _power The digital pin that will power the moisture sensor.
 * @param[in] rawMax The maximum raw analog value. The moisture sensor percentage
 *                   readings will be based on this value.
 */
moisture::moisture(uint8_t _pin, uint8_t _power, int rawMax) {

  // Initialize values and states
  pin = _pin;
  power = _power;
  moistureRawMax = rawMax;

  moisture = 0;
  prevMoisture = 0;
  moistureStatus = 0;
  isConst = false;
  hasFailed = false;

  // Configure power pin
  pinMode(power, OUTPUT);
  digitalWrite(power, LOW);
}

/**
 * Updates the moisture sensor readings
 */
void moisture::read() {
  // Update previous moisture readings
  prevMoisture = moisture;

  // Read the raw moisture readings

  // If the power pin is constantly on...
  if (isConst) {
    //...get the readings from the moisture sensor
    digitalWrite(power, HIGH);
    moistureValue = analogRead(pin);
  } else {
    //...otherwise, turn on the power pin, wait for the sensor to
    // stabilize, get the moisture sensor readings, and turn off
    // the power pin
    digitalWrite(power, HIGH);
    delay(1000);
    moistureValue = analogRead(pin);
    digitalWrite(power, LOW);
  }

  // Get the percentage value of the moisture readings
  moisture = moistureValue / moistureRawMax;
  moisture *= 100.0;

  // Update the status code

  // If the previous moisture readings and the current moisture
  // readings differential is more than the defined trigger value...
  if (abs(prevMoisture - moisture) > TRIGGER_CAUTION){
    //...set the status to caution.
    moistureStatus = STATUS_CAUTION; }
  // If the current moisture readings are below the the trigger
  // value for failure...
  else if (moisture <= TRIGGER_FAILED) {
    //...set the status to failed.
    moistureStatus = STATUS_FAILED;
    hasFailed = true;
  }
  else {
    // The status is OK if there are no triggers
    moistureStatus = STATUS_OK;
  }
}

/**
 * Gets the current moisture sensor readings.
 * 
 * @return The current moisture sensor reading.
 */
double moisture::getReadings(){
  return moisture;
}

/**
 * Gets the previous moisture sensor readings.
 * 
 * @return The previous moisture sensor reading.
 */
double moisture::getPrevReadings(){
  return prevMoisture;
}

/**
 * Gets the current raw analog value of the moisture sensor readings.
 * 
 * @return The current raw analog moisture sensor reading.
 */
double moisture::getRawReadings(){
  return moistureValue;
}

/**
 * Gets the analog pin of the moisture sensor.
 * 
 * @return The analog pin of the moisture sensor.
 */
uint8_t moisture::getPin(){
  return pin;
}

/**
 * The status code of the moisture sensor.
 * 
 * @return If the status is okay, 0 is returned.
 *         If the status is caution, 1 is returned.
 *         If the status is failed, 2 is returned.
 */
int moisture::getStatus(){
  return moistureStatus;
}

/**
 * Gets the failure state of the moisture sensor.
 * 
 * @return True if the moisture sensor has failed, otherwise false.
 */
boolean moisture::getFailed(){
  return hasFailed;
}

/**
 * Sets the power pin to be constantly on or off.
 * 
 * @param[in] _isConst True will keep the power pin constantly on.
 *                     False will power the moisture sensor when
 *                           it is being read
 */
void moisture::setConstPower(boolean _isConst){
  isConst = _isConst;
}

// TODO: Create a function that will minimize the jitters in readings
