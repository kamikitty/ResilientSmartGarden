/**
 * \file moisture.h
 * \brief Moisture sensor logic
 */

#ifndef moisture_h
#define moisture_h

#include <Arduino.h>

class moisture {
  public:
    //////////////////
    // STATUS  CODE //
    //////////////////
    const int STATUS_OK = 0;
    const int STATUS_CAUTION = 1;
    const int STATUS_FAILED = 2;

    ////////////////
    // PROTOTYPES //
    ////////////////
    moisture(uint8_t pin, uint8_t power, int rawMax);
    void read();
    
    double getReadings();
    double getPrevReadings();
    double getRawReadings();
    uint8_t getPin();
    int getStatus();
    boolean getFailed();
    
    void setConstPower(boolean _isConst);

  private:
    // Calibrated readings by having the moisture sensor dry and submerging in
    // a cup of water

    //////////////////////
    // PERCENTAGE RANGE //
    //////////////////////
    const double MOISTURE_PERCENT_MIN = 0.0;
    const double MOISTURE_PERCENT_MAX = 100.0;

    /////////////////////
    // STATUS TRIGGERS //
    /////////////////////
    const int TRIGGER_CAUTION = 10;
    const int TRIGGER_FAILED = 10;

    ////////////////////
    // PIN ASSIGNMENT //
    ////////////////////
    uint8_t pin;
    uint8_t power;

    /////////////////////
    // MOISTURE VALUES //
    /////////////////////
    double moistureRawMax;
    double moistureValue;
    double moisture;
    double prevMoisture;

    /////////////////////
    // MOISTURE STATES //
    /////////////////////
    int moistureStatus;
    boolean isConst;
    boolean hasFailed;
};

#endif

