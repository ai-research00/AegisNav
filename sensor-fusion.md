# Sensor Fusion Module

## Purpose
- Combine magnetometer, accelerometer, and gyroscope readings
- Maintain accurate heading, estimate movement, and support dead-reckoning

## Implementation
1. **Heading Calculation**
   - Fuse compass (magnetometer) with gyro data using a complementary filter
   - Correct for local magnetic interference
2. **Step Counting**
   - Use accelerometer peaks to estimate steps
   - Multiply by stride length (user-configurable)
3. **Dead-Reckoning**
   - Estimate user position from last known map coordinate
   - Correct drift when next landmark/AR anchor is detected
4. **Calibration**
   - Figure-eight motion required to reset magnetometer bias
