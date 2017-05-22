/**
 * \file moisture.cpp
 * \brief Moisture sensor logic
 */

#include "moisture.h"

/**
 * Updates the moisture sensor readings
 */
moisture::read(uint8_t pin){
  double value = analogRead(pin);

  moisture = value / MOISTURE_RAW_MAX;
  moisture *= 100.0;
}

// TODO: Create a function that will minimize the jitters in readings
