/**
 * \file moisture.h
 * \brief Moisture sensor logic
 */

#ifndef moisture_h
#define moisture_h

#include <Arduino.h>

class moisture {
  public:
    moisture(){};
    read(uint8_t pin, uint8_t power);

    double moisture;

  private:
    // Calibrated readings by having the moisture sensor dry and submerging in
    // a cup of water
    const double MOISTURE_RAW_MAX = 715.0;

    const double MOISTURE_PERCENT_MIN = 0.0;
    const double MOISTURE_PERCENT_MAX = 100.0;
};

#endif

