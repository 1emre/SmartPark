

#include <SoftwareSerial.h>
SoftwareSerial mySerial(10, 11); // RX, TX

#define echoPinModule1 9//module_1
#define trigPinModule1 8//module_1


int maximumRange = 100;
int minimumRange = 0;

void setup() {
  
 mySerial.begin(9600);
  pinMode(trigPinModule1, OUTPUT);
  pinMode(echoPinModule1, INPUT);
  
   mySerial.println("setting input module_1");

}
int alinan= 0;
void loop() {
  

  int olcum_module1 = mesafe(maximumRange, minimumRange,trigPinModule1, echoPinModule1);
  mySerial.println(olcum_module1);
  
}

int mesafe(int maxrange, int minrange, int trig, int echo)
{
  long duration, distance;
  digitalWrite(trig, LOW);
  delayMicroseconds(2);
  digitalWrite(trig, HIGH);
  delayMicroseconds(10);
  digitalWrite(trig, LOW);
  duration = pulseIn(echo, HIGH);
  distance = duration / 58.2;
  delay(50);
  if (distance >= maxrange || distance <= minrange)
    return 0;
  return distance;
}
