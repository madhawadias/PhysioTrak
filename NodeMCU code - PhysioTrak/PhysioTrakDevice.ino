#include <Wire.h>
#include <ESP8266WiFi.h>
#include <FirebaseArduino.h>


// Firebase host name and Database secret.
#define FIREBASE_HOST "physiotrakalpha.firebaseio.com"
#define FIREBASE_AUTH "T2uXncC3RJR7qJEsVG0gcdJ487CjGJWWAsJmDTZv"

//Wifi ID and password to connect to the network
#define WIFI_SSID "BHN"
#define WIFI_PASSWORD "567890BHN"
int n = 0;

// MPU6050 Slave Device Address
double Accelerometer_x, Accelerometer_y, Accelerometer_z, T, Gyrometer_x, Gyrometer_y, Gyrometer_z;
const uint8_t MPU6050SlaveAddress = 0x68;

// Select SDA and SCL pins for I2C communication 
const uint8_t scl = 12;  //D6
const uint8_t sda = 14;   //D5

// sensitivity scale factor respective to full scale setting provided in datasheet 
const uint16_t AccelScaleFactor = 16384;
const uint16_t GyroScaleFactor = 131;

// MPU6050 few configuration register addresses
const uint8_t MPU6050_REGISTER_SMPLRT_DIV   =  0x19;
const uint8_t MPU6050_REGISTER_USER_CTRL    =  0x6A;
const uint8_t MPU6050_REGISTER_PWR_MGMT_1   =  0x6B;
const uint8_t MPU6050_REGISTER_PWR_MGMT_2   =  0x6C;
const uint8_t MPU6050_REGISTER_CONFIG       =  0x1A;
const uint8_t MPU6050_REGISTER_GYRO_CONFIG  =  0x1B;
const uint8_t MPU6050_REGISTER_ACCEL_CONFIG =  0x1C;
const uint8_t MPU6050_REGISTER_FIFO_EN      =  0x23;
const uint8_t MPU6050_REGISTER_INT_ENABLE   =  0x38;
const uint8_t MPU6050_REGISTER_ACCEL_XOUT_H =  0x3B;
const uint8_t MPU6050_REGISTER_SIGNAL_PATH_RESET  = 0x68;

int16_t AccelX, AccelY, AccelZ, Temperature, GyroX, GyroY, GyroZ;


//below Flex sensor part

const int FLEX_PIN = A0; // Pin connected to voltage divider output

// Measure the voltage at 5V and the actual resistance of 10k ohm resistor

const float VCC = 4.98; // Measured voltage of Ardunio 5V line
const float R_DIV = 47500.0; // Measured resistance of 3.3k resistor

// Adjust values to more accurately calculate bend degree.

const float STRAIGHT_RESISTANCE = 37300.0; // resistance when straight
const float BEND_RESISTANCE = 90000.0; // resistance at 90 deg

//variables used in sensors
float angle;
float flexR;



void setup() {

  Serial.begin(9600);
  Wire.begin(sda, scl);
  MPU6050_Init();

  //flex sensor
  pinMode(FLEX_PIN, INPUT);


  
  
  // connect to wifi.
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("connecting");
  while (WiFi.status() != WL_CONNECTED) {
    Serial.print(".");
    delay(500);
  }
  Serial.println();
  Serial.print("connected: ");
  Serial.println(WiFi.localIP());

  //establish firebase connection 
  //Security Risk : Protection of data of users physical movements
  Firebase.begin(FIREBASE_HOST, FIREBASE_AUTH);

  
}

//Inserting to Firebase database in relatime by updating records
void updateDatabase(){
  
  Firebase.setInt("Angle", angle);
  Firebase.setFloat("Accelerometer_x", Accelerometer_x);
  Firebase.setFloat("Accelerometer_y", Accelerometer_y);
  Firebase.setFloat("Accelerometer_z", Accelerometer_z);

  Firebase.setFloat("Gyrometer_x", Gyrometer_x);
  Firebase.setFloat("Gyrometer_y", Gyrometer_y);
  Firebase.setFloat("Gyrometer_z", Gyrometer_z);
 
  // handle error
  if (Firebase.failed()) {
      Serial.print("pushing /logs failed:");
      Serial.println(Firebase.error());  
      return;
  }
}


void loop() {

  getMPUReading();
  getFlexReading();
  printInSerial();
  //HCI : Real time update of reading to database so the User can track movement in realtime. 
updateDatabase();
  delay(100);
  
}

void I2C_Write(uint8_t deviceAddress, uint8_t regAddress, uint8_t data){
  Wire.beginTransmission(deviceAddress);
  Wire.write(regAddress);
  Wire.write(data);
  Wire.endTransmission();
}

// read all 14 register
void Read_RawValue(uint8_t deviceAddress, uint8_t regAddress){
  Wire.beginTransmission(deviceAddress);
  Wire.write(regAddress);
  Wire.endTransmission();
  Wire.requestFrom(deviceAddress, (uint8_t)14);
  AccelX = (((int16_t)Wire.read()<<8) | Wire.read());
  AccelY = (((int16_t)Wire.read()<<8) | Wire.read());
  AccelZ = (((int16_t)Wire.read()<<8) | Wire.read());
  Temperature = (((int16_t)Wire.read()<<8) | Wire.read());
  GyroX = (((int16_t)Wire.read()<<8) | Wire.read());
  GyroY = (((int16_t)Wire.read()<<8) | Wire.read());
  GyroZ = (((int16_t)Wire.read()<<8) | Wire.read());
}

//configure MPU6050
void MPU6050_Init(){
  delay(1000);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_SMPLRT_DIV, 0x07);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_PWR_MGMT_1, 0x01);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_PWR_MGMT_2, 0x00);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_CONFIG, 0x00);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_GYRO_CONFIG, 0x00);//set +/-250 degree/second full scale
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_ACCEL_CONFIG, 0x00);// set +/- 2g full scale
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_FIFO_EN, 0x00);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_INT_ENABLE, 0x01);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_SIGNAL_PATH_RESET, 0x00);
  I2C_Write(MPU6050SlaveAddress, MPU6050_REGISTER_USER_CTRL, 0x00);
}

void getMPUReading(){
  Read_RawValue(MPU6050SlaveAddress, MPU6050_REGISTER_ACCEL_XOUT_H);
  
  //divide each with their sensitivity scale factor
  Accelerometer_x = (double)AccelX/AccelScaleFactor;
  Accelerometer_y = (double)AccelY/AccelScaleFactor;
  Accelerometer_z = (double)AccelZ/AccelScaleFactor;
  T = (double)Temperature/340+36.53;      //temperature formula
  Gyrometer_x = (double)GyroX/GyroScaleFactor;
  Gyrometer_y = (double)GyroY/GyroScaleFactor;
  Gyrometer_z = (double)GyroZ/GyroScaleFactor;
  
  }

//flex sensor 
void getFlexReading(){
    
// Read the ADC, and calculate voltage and resistance from it
  int flexADC = analogRead(FLEX_PIN);
  float flexV = flexADC * VCC / 1023.0;
  flexR = R_DIV * (VCC / flexV - 1.0);


  // Use the calculated resistance to estimate the sensor's bend angle:

  angle = map(flexR, STRAIGHT_RESISTANCE, BEND_RESISTANCE,
                   0, 90.0);
                   
    }
//Print output data in serial monitor
void printInSerial(){
      Serial.print("Accelerometer_x: "); Serial.print(Accelerometer_x);
  Serial.print(" Accelerometer_y: "); Serial.print(Accelerometer_y);
  Serial.print(" Accelerometer_z: "); Serial.print(Accelerometer_z);
  Serial.print(" Temperature: "); Serial.print(T);
  Serial.print(" Gyrometer_x: "); Serial.print(Gyrometer_x);
  Serial.print(" Gyrometer_y: "); Serial.print(Gyrometer_y);
  Serial.print(" Gyrometer_z: "); Serial.println(Gyrometer_z);
  Serial.println("Resistance: " + String(flexR) + " ohms");
  Serial.println("Bend: " + String(angle) + " degrees");
  Serial.println();
      }
